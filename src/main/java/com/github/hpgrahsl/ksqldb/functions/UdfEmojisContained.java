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

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import io.confluent.ksql.function.udf.UdfParameter;

import java.util.HashSet;
import java.util.List;

@UdfDescription(
        name = "emojis_contained",
        description = "leverages the emoji-java library to check if a string contains emojis",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisContained {

    @Udf(description = "checks whether or not the given string contains emojis")
    public Boolean containsEmojis(
            @UdfParameter(value = "text", description = "the given text in which to check for any(!) emoji occurrences")
            final String text) {

        return text == null ? null : EmojiManager.containsEmoji(text);

    }

    @Udf(description = "checks whether or not the given string contains emojis")
    public Boolean containsEmojis(
            @UdfParameter(value = "text", description = "the given text in which to check for any of the specified emoji occurrences")
            final String text,
            @UdfParameter(value = "specificEmojis", description = "a list of specific emojis to look for")
            final List<String> specificEmojis) {

        if(text == null)
            return null;

        var containedEmojis = new HashSet<>(EmojiParser.extractEmojis(text));
        containedEmojis.retainAll(new HashSet<>(specificEmojis));
        return containedEmojis.size() > 0;

    }

}
