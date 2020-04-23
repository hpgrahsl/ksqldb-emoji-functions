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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UdfDescription(
        name = "emojis_to_aliases",
        description = "leverages the emoji-java library to replace emojis contained in a string by their textual aliases",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisToAliases {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdfEmojisToAliases.class);

    @Udf(description = "replace emojis contained in a string by their textual aliases")
    public String replaceEmojisWithAliases(
            @UdfParameter(value = "text", description = "the given text in which to replace any(!) emojis by their textual aliases")
            final String text,
            @UdfParameter(value = "fpAction",description = "how to deal with Fitzpatrick modifiers, must be either PARSE, REMOVE or IGNORE")
            final String fpAction) {

        if(text == null || fpAction == null) {
            LOGGER.warn("any of the UDF parameters ('text','fpAction') was null which is probably not intended");
            return null;
        }

        try {
            return EmojiParser.parseToAliases(text, EmojiParser.FitzpatrickAction.valueOf(fpAction.toUpperCase()));
        } catch(IllegalArgumentException e) {
            LOGGER.error("the UDF parameter (fpAction '"+fpAction+"') is invalid", e);
            return null;
        }

    }

}
