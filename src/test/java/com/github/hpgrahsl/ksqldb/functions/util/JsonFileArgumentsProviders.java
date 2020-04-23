/*
 * Copyright (c) 2020. Hans-Peter Grahsl (grahslhp@gmail.com)
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: https://opensource.org/licenses/MIT
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.github.hpgrahsl.ksqldb.functions.util;

import org.junit.jupiter.params.provider.Arguments;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JsonFileArgumentsProviders {

    private static final String SAMPLES_EMOJIS_EXTRACT = "./udf_emojis_extract_samples.json";
    private static final String SAMPLES_EMOJIS_COUNT = "./udf_emojis_count_samples.json";
    private static final String SAMPLES_EMOJIS_CONTAINED_ANY = "./udf_emojis_contained_any_samples.json";
    private static final String SAMPLES_EMOJIS_CONTAINED_SPECIFIC = "./udf_emojis_contained_specific_samples.json";
    private static final String SAMPLES_EMOJIS_REMOVE_ANY = "./udf_emojis_remove_any_samples.json";
    private static final String SAMPLES_EMOJIS_REMOVE_SPECIFIC = "./udf_emojis_remove_specific_samples.json";
    private static final String SAMPLES_EMOJIS_TO_ALIASES = "./udf_emojis_to_aliases_samples.json";
    private static final String SAMPLES_EMOJIS_TO_HTMLCODEPOINTS = "./udf_emojis_to_htmlcodepoints_samples.json";

    private static List<JsonObject> parseJsonSampleFile(String filePath) {
        try (var jr = Json.createReader(
                new FileInputStream(Objects.requireNonNull(JsonFileArgumentsProviders.class
                        .getClassLoader().getResource(filePath)).getFile()))) {
            return jr.readArray().getValuesAs(JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Stream<Arguments> emojisExtractSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_EXTRACT)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.getBoolean("unique"),
                        jo.get("result").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getJsonArray("result").getValuesAs(js -> js.toString().replaceAll("\"",""))
                        )
                );
    }

    public static Stream<Arguments> emojisCountSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_COUNT)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.getBoolean("unique"),
                        jo.get("result").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getInt("result")
                        )
                );
    }

    public static Stream<Arguments> emojisContainedAnySamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_CONTAINED_ANY)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.get("result").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getBoolean("result")
                        )
                );
    }

    public static Stream<Arguments> emojisContainedSpecificSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_CONTAINED_SPECIFIC)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.get("specificEmojis").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getJsonArray("specificEmojis").getValuesAs(js -> js.toString().replaceAll("\"","")),
                        jo.get("result").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getBoolean("result")
                        )
                );
    }

    public static Stream<Arguments> emojisRemoveAnySamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_REMOVE_ANY)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.getString("result",null)
                        )
                );
    }

    public static Stream<Arguments> emojisRemoveSpecificSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_REMOVE_SPECIFIC)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.get("specificEmojis").getValueType().equals(JsonValue.ValueType.NULL)
                                ? null
                                : jo.getJsonArray("specificEmojis").getValuesAs(js -> js.toString().replaceAll("\"","")),
                        jo.getString("result",null)
                        )
                );
    }

    public static Stream<Arguments> emojisToAliasesSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_TO_ALIASES)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.getString("fpAction",null),
                        jo.getString("result",null)
                        )
                );
    }

    public static Stream<Arguments> emojisToHtmlCodepointsSamples() {
        return parseJsonSampleFile(SAMPLES_EMOJIS_TO_HTMLCODEPOINTS)
                .stream()
                .map(jo -> arguments(
                        jo.getString("text",null),
                        jo.getString("fpAction",null),
                        jo.getString("encoding",null),
                        jo.getString("result",null)
                        )
                );
    }

}
