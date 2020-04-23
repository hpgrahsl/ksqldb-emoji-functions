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
        name = "emojis_to_htmlcodepoints",
        description = "leverages the emoji-java library to replace emojis contained in a string by their HTML codepoints",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisToHtmlCodepoints {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdfEmojisToHtmlCodepoints.class);

    public enum EncodingStyle {
        HEX,
        DEC
    }

    @Udf(description = "replace emojis contained in a string by their HTML codepoints")
    public String replaceEmojisWithCodepoints(
            @UdfParameter(value = "text", description = "the given text in which to replace any(!) emojis by their HTML codepoints")
            final String text,
            @UdfParameter(value = "fpAction", description = "how to deal with Fitzpatrick modifiers, must be one of: PARSE, REMOVE, IGNORE")
            final String fpAction,
            @UdfParameter(value = "encoding", description = "which HTML codepoints representation to use, must be one of: HEX, DEC")
            final String encoding) {

        if(text == null || fpAction == null || encoding == null) {
            LOGGER.warn("any of the UDF parameters ('text','fpAction','encoding') was null which is probably not intended");
            return null;
        }

        try {
            switch (EncodingStyle.valueOf(encoding)) {
                case DEC:
                    return EmojiParser.parseToHtmlDecimal(text, EmojiParser.FitzpatrickAction.valueOf(fpAction.toUpperCase()));
                case HEX:
                    return EmojiParser.parseToHtmlHexadecimal(text, EmojiParser.FitzpatrickAction.valueOf(fpAction.toUpperCase()));
            }
        } catch(IllegalArgumentException e) {
            LOGGER.error("any of the UDF parameters (fpAction '"+fpAction+"' or encoding '"+encoding+"') is invalid", e);
        }

        return null;

    }

}
