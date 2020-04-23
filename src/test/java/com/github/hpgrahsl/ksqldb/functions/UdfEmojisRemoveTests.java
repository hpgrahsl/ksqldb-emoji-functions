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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UdfEmojisRemoveTests {

  @DisplayName("applying UDF...")
  @ParameterizedTest(name = "emojis_remove({0}) = {1}")
  @MethodSource("com.github.hpgrahsl.ksqldb.functions.util.JsonFileArgumentsProviders#emojisRemoveAnySamples")
  void applyUdfEmojisRemove(String text, String result) {
    assertEquals(result,new UdfEmojisRemove().removeEmojis(text),"unexpected string mismatch after removing all emojis");
  }

  @DisplayName("applying UDF...")
  @ParameterizedTest(name = "emojis_remove({0},{1}) = {2}")
  @MethodSource("com.github.hpgrahsl.ksqldb.functions.util.JsonFileArgumentsProviders#emojisRemoveSpecificSamples")
  void applyUdfEmojisRemove(String text, List<String> specificEmojis, String result) {
      assertEquals(result,new UdfEmojisRemove().removeEmojis(text,specificEmojis),"unexpected string mismatch after removing all emojis");
  }

}