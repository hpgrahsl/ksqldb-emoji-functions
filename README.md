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

### Installation / Deployment

1. You can either build the Maven project from sources or download the latest snapshot release as self-contained jar from [here](https://drive.google.com/file/d/167vjGKp99cQfppfWh5YrrBziI2Kiua0l/view?usp=sharing).
2. Move the `emoji-functions-1.0-SNAPSHOT.jar` file into a folder of your ksqlDB installation that is configured to load custom functions from during server bootstrap.
3. (Re)Start your ksqlDB server instance(s) to make it pick up and load the emoji functions.
4. Verify if the deployment was successful by opening a ksqlDB CLI session and running `SHOW FUNCTIONS;` which should amongst all other available functions list the following **emoji-related UDFs**:

```
 Function Name         | Type      
-----------------------------------
 ...
 EMOJIS_CONTAINED      | SCALAR    
 EMOJIS_COUNT          | SCALAR    
 EMOJIS_EXTRACT        | SCALAR    
 EMOJIS_REMOVE         | SCALAR    
 ...
-----------------------------------
```

##### HAVE FUN working with 🚀Emojis in ksqlDB🚀

### Credits

Thanks for [Vincent Durmont's](http://www.vdurmont.com/) great [emoji-java](https://github.com/vdurmont/emoji-java) library which is used to do the _hard emoji work_ behind the scenes!
