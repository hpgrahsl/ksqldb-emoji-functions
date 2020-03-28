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

package com.github.hpgrahsl.ksqldb.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class UdfEmojisExtractTests {

  @TestFactory
  @DisplayName("applying UDF...")
  Stream<DynamicTest> extractEmojis() {

    var udf = new UdfEmojisExtract();

    var testData = Map.of(
            Map.entry("",false), Collections.emptyList(),
            Map.entry("",true), Collections.emptyList(),
            Map.entry("some text without emojis",false), Collections.emptyList(),
            Map.entry("some text without emojis",true), Collections.emptyList(),
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",false), List.of("😎","🤞","🤓","😍","😍","🚀","🚀"),
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",true), List.of("😎","🤞","🤓","😍","🚀"),
            Map.entry("🤓🤓🤓😍😍",false), List.of("🤓","🤓","🤓","😍","😍"),
            Map.entry("🤓🤓🤓😍😍",true), List.of("🤓","😍")
    );

    return testData.entrySet().stream().map(es -> dynamicTest(
            "emojis_extract(\""+es.getKey().getKey()+"\","+es.getKey().getValue()+") = "+es.getValue(),
            () -> assertEquals(es.getValue(),udf.extractEmojis(es.getKey().getKey(),es.getKey().getValue()),
                          "unexpected mismatch of extracted emojis")
    ));

  }

}