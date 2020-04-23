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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UdfEmojisToHtmlCodepointsTests {

  @DisplayName("applying UDF...")
  @ParameterizedTest(name = "emojis_to_htmlcodepoints({0},{1},{2}) = {3}")
  @MethodSource("com.github.hpgrahsl.ksqldb.functions.util.JsonFileArgumentsProviders#emojisToHtmlCodepointsSamples")
  void applyUdfEmojisToHtmlCodepoints(String text, String fpAction, String encoding, String result) {
    assertEquals(result,new UdfEmojisToHtmlCodepoints().replaceEmojisWithCodepoints(text,fpAction,encoding), "unexpected string mismatch after replacing emojis with html codepoints");
  }

}