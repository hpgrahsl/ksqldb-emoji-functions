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

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class UdfEmojisCountTests {

  @TestFactory
  @DisplayName("applying UDF...")
  Stream<DynamicTest> countEmojis() {

    var udf = new UdfEmojisCount();

    var testData = Map.of(
            Map.entry("",false), 0,
            Map.entry("",true), 0,
            Map.entry("some text without emojis",false), 0,
            Map.entry("some text without emojis",true), 0,
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",false), 7,
            Map.entry("😎🤞some 🤓 text 😍 with 😍 emojis🚀🚀rocks!",true), 5,
            Map.entry("🤓🤓🤓😍😍",false), 5,
            Map.entry("🤓🤓🤓😍😍",true), 2
    );

    return testData.entrySet().stream().map(es -> dynamicTest(
            "emojis_count(\""+es.getKey().getKey()+"\","+es.getKey().getValue()+") = "+es.getValue(),
            () -> assertEquals(es.getValue(),udf.countEmojis(es.getKey().getKey(),es.getKey().getValue()),
                    "unexpected mismatch of counted emojis")
    ));

  }

}