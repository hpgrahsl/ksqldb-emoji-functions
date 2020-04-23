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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@UdfDescription(
        name = "emojis_remove",
        description = "leverages the emoji-java library to remove emojis contained in a string",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisRemove {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdfEmojisRemove.class);

    @Udf(description = "removes emojis contained in a string")
    public String removeEmojis(
            @UdfParameter(value = "text", description = "the given text from which to remove any(!) emojis")
            final String text) {

        if(text == null) {
            LOGGER.warn("the UDF parameter ('text') was null which is probably not intended");
            return null;
        }
        return EmojiParser.removeAllEmojis(text);

    }

    @Udf(description = "removes emojis contained in a string")
    public String removeEmojis(
            @UdfParameter(value = "text", description = "the given text from which to remove any of the specified  emojis")
            final String text,
            @UdfParameter(value = "specificEmojis", description = "a list of specific emojis to remove")
            final List<String> specificEmojis) {

        if(text == null || specificEmojis == null) {
            LOGGER.warn("any of the UDF parameters ('text','specificEmojis') was null which is probably not intended");
            return null;
        }

        return EmojiParser.removeEmojis(text,
                        specificEmojis.stream()
                                .map(EmojiManager::getByUnicode)
                                .collect(Collectors.toList())
        );

    }

}
