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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@UdfDescription(
        name = "emojis_extract",
        description = "leverages the emoji-java library to extract emojis from strings",
        author = "Hans-Peter Grahsl (follow @hpgrahsl)",
        version = "1.0.0"
)
public class UdfEmojisExtract {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdfEmojisExtract.class);

    @Udf(description = "extracts a list of potentially contained emojis with or without duplicates from the given string")
    public List<String> extractEmojis(
            @UdfParameter(value = "text", description = "the given text to extract emojis from")
            final String text,
            @UdfParameter(value = "unique", description = "if true will return only unique emojis (set semantic), if false every emoji i.e. also duplicate ones (list semantic) will be returned")
            final boolean unique) {

        if(text == null) {
            LOGGER.warn("the UDF parameter ('text') was null which is probably not intended");
            return null;
        }

        return !unique
                ? EmojiParser.extractEmojis(text)
                : new ArrayList<>(new LinkedHashSet<>(EmojiParser.extractEmojis(text)));

    }

}
