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

import com.vdurmont.emoji.EmojiParser;
import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import io.confluent.ksql.function.udf.UdfParameter;

import java.util.HashSet;

@UdfDescription(
        name = "emojis_count",
        description = "leverages the emoji-java library to count emojis within strings",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisCount {

    @Udf(description = "counts the number of potentially contained emojis with or without duplicates from the given string")
    public Integer countEmojis(
            @UdfParameter(value = "text", description = "the given text in which to count emojis")
            final String text,
            @UdfParameter(value = "unique", description = "if true will return count of unique emojis, if false counts all emojis i.e. also duplicates")
            final boolean unique) {

        if(text == null)
            return null;

        return !unique
                ? EmojiParser.extractEmojis(text).size()
                : new HashSet<>(EmojiParser.extractEmojis(text)).size();

    }

}
