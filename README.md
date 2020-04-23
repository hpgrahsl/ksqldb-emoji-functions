# Emoji Functions for ksqlDB

This project provides custom [ksqlDB](https://ksqldb.io/) user-defined functions (UDFs) which help to handle [emojis](https://en.wikipedia.org/wiki/Emoji) contained in text.

### Available Functions

Currently it provides the following UDFs:

##### EMOJIS_CONTAINED

```
Overview    : leverages the emoji-java library to check if a string contains emojis
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_CONTAINED(text VARCHAR)
	Returns     : BOOLEAN
	Description : checks whether or not the given string contains emojis
	text        : the given text in which to check for any(!) emoji occurrences

	Variation   : EMOJIS_CONTAINED(text VARCHAR, specificEmojis ARRAY<VARCHAR>)
	Returns     : BOOLEAN
	Description : checks whether or not the given string contains emojis
	text        : the given text in which to check for any of the specified emoji occurrences
	specificEmojis: a list of specific emojis to look for
```

##### EMOJIS_COUNT

```
Overview    : leverages the emoji-java library to count emojis within strings
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_COUNT(text VARCHAR, unique BOOLEAN)
	Returns     : INT
	Description : counts the number of potentially contained emojis with or without duplicates from the given string
	text        : the given text in which to count emojis
	unique      : if true will return count of unique emojis, if false counts all emojis i.e. also duplicates
```

##### EMOJIS_EXTRACT

```
Overview    : leverages the emoji-java library to extract emojis from strings
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_EXTRACT(text VARCHAR, unique BOOLEAN)
	Returns     : ARRAY<VARCHAR>
	Description : extracts a list of potentially contained emojis with or without duplicates from the given string
	text        : the given text to extract emojis from
	unique      : if true will return only unique emojis (set semantic), if false every emoji i.e. also duplicate ones (list semantic) will be returned
```

##### EMOJIS_REMOVE

```
Overview    : leverages the emoji-java library to remove emojis contained in a string
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_REMOVE(text VARCHAR)
	Returns     : VARCHAR
	Description : removes emojis contained in a string
	text        : the given text from which to remove any(!) emojis

	Variation   : EMOJIS_REMOVE(text VARCHAR, specificEmojis ARRAY<VARCHAR>)
	Returns     : VARCHAR
	Description : removes emojis contained in a string
	text        : the given text from which to remove any of the specified  emojis
	specificEmojis: a list of specific emojis to remove
```

##### EMOJIS_TO_ALIASES

```
Overview    : leverages the emoji-java library to replace emojis contained in a string by their textual aliases
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_TO_ALIASES(text VARCHAR, fpAction VARCHAR)
	Returns     : VARCHAR
	Description : replace emojis contained in a string by their textual aliases
	text        : the given text in which to replace any(!) emojis by their textual aliases
	fpAction    : how to deal with Fitzpatrick modifiers, must be either PARSE, REMOVE or IGNORE
```

##### EMOJIS_TO_HTMLCODEPOINTS

```
Version     : 1.0.0
Overview    : leverages the emoji-java library to replace emojis contained in a string by their HTML codepoints
Type        : SCALAR
Variations  : 

	Variation   : EMOJIS_TO_HTMLCODEPOINTS(text VARCHAR, fpAction VARCHAR, encoding VARCHAR)
	Returns     : VARCHAR
	Description : replace emojis contained in a string by their HTML codepoints
	text        : the given text in which to replace any(!) emojis by their HTML codepoints
	fpAction    : how to deal with Fitzpatrick modifiers, must be one of: PARSE, REMOVE, IGNORE
	encoding    : which HTML codepoints representation to use, must be one of: HEX, DEC
```

### Examples

The UDF call examples below are based on the following pre-defined sample content:

```sql
-- 'create stream with example content'
CREATE STREAM examples
    (id VARCHAR, content VARCHAR)
WITH (kafka_topic='examples',value_format='JSON',partitions=1,replicas=1,key='id');

-- 'insert a few records with or without emojis'
INSERT INTO examples (id) VALUES ('1');
INSERT INTO examples (id,content) VALUES ('2','');
INSERT INTO examples (id,content) VALUES ('3','This is text without any emojis.');
INSERT INTO examples (id,content) VALUES ('4','🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏');

-- 'have fun with the EMOJI UDFs!'
```

##### EMOJIS_CONTAINED

```
ksql> SELECT id,content,EMOJIS_CONTAINED(content) AS result FROM examples EMIT CHANGES;
+----------------------------------------------------------------------------+----------------------------------------------------------------------------+----------------------------------------------------------------------------+
|ID                                                                          |CONTENT                                                                     |RESULT                                                                      |
+----------------------------------------------------------------------------+----------------------------------------------------------------------------+----------------------------------------------------------------------------+
|1                                                                           |null                                                                        |null                                                                        |
|2                                                                           |                                                                            |false                                                                       |
|3                                                                           |This is text without any emojis.                                            |false                                                                       |
|4                                                                           |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏                          |true                                                                        |
^CQuery terminated
```

##### EMOJIS_COUNT

```
ksql> SELECT id,content,EMOJIS_COUNT(content,false) AS result1,EMOJIS_COUNT(content,true) AS result2 FROM examples EMIT CHANGES;
+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+
|ID                                                       |CONTENT                                                  |RESULT1                                                  |RESULT2                                                  |
+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+
|1                                                        |null                                                     |null                                                     |null                                                     |
|2                                                        |                                                         |0                                                        |0                                                        |
|3                                                        |This is text without any emojis.                         |0                                                        |0                                                        |
|4                                                        |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏       |12                                                       |8                                                        |
^CQuery terminated
```

##### EMOJIS_EXTRACT

```
ksql> SELECT id,content,EMOJIS_EXTRACT(content,false) AS result1,EMOJIS_EXTRACT(content,true) AS result2 FROM examples EMIT CHANGES;
+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+
|ID                                                       |CONTENT                                                  |RESULT1                                                  |RESULT2                                                  |
+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+---------------------------------------------------------+
|1                                                        |null                                                     |null                                                     |null                                                     |
|2                                                        |                                                         |[]                                                       |[]                                                       |
|3                                                        |This is text without any emojis.                         |[]                                                       |[]                                                       |
|4                                                        |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏       |[🤓, 🤓, 🤩, 🌻, 🌺, 🍄, 🍄, 🎸, 🚀, 🚀, 🚀, 👏]     |[🤓, 🤩, 🌻, 🌺, 🍄, 🎸, 🚀, 👏]                         |
^CQuery terminated
```

##### EMOJIS_REMOVE

```
ksql> SELECT id,content,EMOJIS_REMOVE(content) AS result FROM examples EMIT CHANGES;
+----------------------------------------------------------------------------+----------------------------------------------------------------------------+----------------------------------------------------------------------------+
|ID                                                                          |CONTENT                                                                     |RESULT                                                                      |
+----------------------------------------------------------------------------+----------------------------------------------------------------------------+----------------------------------------------------------------------------+
|1                                                                           |null                                                                        |null                                                                        |
|2                                                                           |                                                                            |                                                                            |
|3                                                                           |This is text without any emojis.                                            |This is text without any emojis.                                            |
|4                                                                           |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏                          |This  is textwithemojis.                                                    |
^CQuery terminated
```

##### EMOJIS_TO_ALIASES

```
ksql> SELECT id,content,EMOJIS_TO_ALIASES(content,'PARSE') AS result FROM examples EMIT CHANGES;
+--------------------------------------------------------+--------------------------------------------------------+--------------------------------------------------------+
|ID                                                      |CONTENT                                                 |RESULT                                                  |
+--------------------------------------------------------+--------------------------------------------------------+--------------------------------------------------------+
|1                                                       |null                                                    |null                                                    |
|2                                                       |                                                        |                                                        |
|3                                                       |This is text without any emojis.                        |This is text without any emojis.                        |
|4                                                       |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀.👏      |:nerd::nerd:This :star_struck: is text:sunflower::hibisc|
|                                                        |                                                        |us::mushroom::mushroom:with:guitar::rocket:emojis:rocket|
|                                                        |                                                        |::rocket:.:clap:                                        |
^CQuery terminated
```

##### EMOJIS_TO_HTMLCODEPOINTS

```
+---------------------------------------------+-------------------------------------------------+---------------------------------------------+---------------------------------------------+
|ID                                           |CONTENT                                          |RESULT1                                      |RESULT2                                      |
+---------------------------------------------+-------------------------------------------------+---------------------------------------------+---------------------------------------------+
|1                                            |null                                             |null                                         |null                                         |
|2                                            |                                                 |                                             |                                             |
|3                                            |This is text without any emojis.                 |This is text without any emojis.             |This is text without any emojis.             |
|4                                            |🤓🤓This 🤩 is text🌻🌺🍄🍄with🎸🚀emojis🚀🚀|&#x1f913;&#x1f913;This &#x1f929; is text&#x1f|&#129299;&#129299;This &#129321; is text&#127|
|                                             |.👏                                              |33b;&#x1f33a;&#x1f344;&#x1f344;with&#x1f3b8;&|803;&#127802;&#127812;&#127812;with&#127928;&|
|                                             |                                                  |#x1f680;emojis&#x1f680;&#x1f680;.&#x1f44f;   |#128640;emojis&#128640;&#128640;.&#128079;   |
^CQuery terminated
```

### Installation / Deployment

1. You can either build the Maven project from sources or download the latest release as self-contained jar from [here](https://drive.google.com/file/d/1NkXitI9fer6OmqVsFZnMPhDGHNo2tsu9/view?usp=sharing).
2. Move the `emoji-functions-1.0.jar` file into a folder of your ksqlDB installation that is configured to load custom functions from during server bootstrap.
3. (Re)Start your ksqlDB server instance(s) to make it pick up and load the emoji functions.
4. Verify if the deployment was successful by opening a ksqlDB CLI session and running `SHOW FUNCTIONS;` which should amongst all other available functions list the following **emoji-related UDFs**:

```
 Function Name            | Type      
-----------------------------------
 ...
 EMOJIS_CONTAINED         | SCALAR    
 EMOJIS_COUNT             | SCALAR    
 EMOJIS_EXTRACT           | SCALAR    
 EMOJIS_REMOVE            | SCALAR 
 EMOJIS_TO_ALIASES        | SCALAR    
 EMOJIS_TO_HTMLCODEPOINTS | SCALAR    
 ...
-----------------------------------
```

##### HAVE FUN working with 🚀Emojis in ksqlDB🚀

### Credits

Thanks for [Vincent Durmont's](http://www.vdurmont.com/) great [emoji-java](https://github.com/vdurmont/emoji-java) library which is used to do the _hard emoji work_ behind the scenes!
