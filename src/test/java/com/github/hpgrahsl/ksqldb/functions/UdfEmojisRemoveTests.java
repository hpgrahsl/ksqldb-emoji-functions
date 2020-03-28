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

public class UdfEmojisRemoveTests {

  @TestFactory
  @DisplayName("applying UDF...")
  Stream<DynamicTest> removeAnyEmojis() {

    var udf = new UdfEmojisRemove();

    var testDataAny = Map.of(
            "", "",
            "some text without emojis","some text without emojis",
            "😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!","some  text  with  emojisrocks!",
            "🤓🤓🤓😍😍",""
    );

    return testDataAny.entrySet().stream().map(es -> dynamicTest(
            "emojis_remove(\""+es.getKey()+"\") = \""+es.getValue()+"\"",
            () -> assertEquals(es.getValue(),udf.removeEmojis(es.getKey()),
                    "unexpected string mismatch after removing all emojis")
    ));

  }

  @TestFactory
  @DisplayName("applying UDF...")
  Stream<DynamicTest> removeSpecificEmojis() {

    var udf = new UdfEmojisRemove();

    var testDataSpecific = Map.of(
            Map.entry("", Collections.<String>emptyList()), "",
            Map.entry("", List.of("😱","🧐","😍")), "",
            Map.entry("some text without emojis", Collections.<String>emptyList()), "some text without emojis",
            Map.entry("some text without emojis",List.of("😱","🧐","😍")),"some text without emojis",
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",List.of("😍")),"😎🤞some 🤓 text  with  emojis🚀🚀rocks!",
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",List.of("😎","🤞","🤓","😍","🚀")),"some  text  with  emojisrocks!",
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",Collections.<String>emptyList()),"😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",
            Map.entry("🤓🤓🤓😍😍",List.of("🤓","😍")),"",
            Map.entry("🤓🤓🤓😍😍",List.of("😱","🧐")),"🤓🤓🤓😍😍",
            Map.entry("🤓🤓🤓😍😍",Collections.<String>emptyList()),"🤓🤓🤓😍😍"
    );

    return testDataSpecific.entrySet().stream().map(es -> dynamicTest(
            "emojis_remove(\""+es.getKey().getKey()+"\","+es.getKey().getValue()+") = \""+es.getValue()+"\"",
            () -> assertEquals(es.getValue(),udf.removeEmojis(es.getKey().getKey(),es.getKey().getValue()),
                    "unexpected string mismatch after removing specific emojis")
    ));

  }

}