CI on master: [![Build Status](https://travis-ci.com/lambico/lambico-datatest.svg?branch=master)](https://travis-ci.com/lambico/lambico-datatest)

CI on develop: [![Build Status](https://travis-ci.com/lambico/lambico-datatest.svg?branch=develop)](https://travis-ci.com/lambico/lambico-datatest)


# Lambico Datatest

Support for data-centric tests.

# Introduction

**Warning:** at present we are still experimenting, so all the code, project structure, data format, really everything could change.

The main objective is to build a library for easily supporting data-centric tests, i.e. tests using data stored somewhere and accesed through some protocol, library or framework.

In the first stage we will support tests on a relational DBMS, accessed through JPA.

One of the problems in this situation is usually the loading of the test data set before the starting of the test.

There are some libraries (for example [DBUnit](http://dbunit.sourceforge.net/)), but usualy they are too related to RDBMs, and with too verbose formats for test datasets.

We will start supporting at least JSON as main format for datasets.

For example, in the simplest case, if we have to populate a database containing two entities (`org.lambico.datatest.example1.model.Entity1` and `org.lambico.datatest.example1.model.Entity2`), we will aim to have in a directory `org/lambico/datatest.example1.model` two JSON files:

**`Entity1.json`**:
```json
[
    {
        "@id" : "Entity1-1",
        "id" : null,
        "stringField" : "test1",
        "entity2" : {
            "@ref" : "Entity2-1"
        }
    },
    {
        "@id" : "Entity1-2",
        "id" : null,
        "stringField" : "test3",
        "entity2" : {
            "@ref" : "Entity2-1"
        }
    }
]
```

**`Entity2.json`**:
```json
[
    {
        "@id" : "Entity2-1",
        "id" : null,
        "stringField" : "test2",
        "entity1" : {
            "@ref" : "Entity1-1"
        }
    }
]
```

or, if you prefer a single file as dataset, you could have:

**`dataset.json`**:
```json
{
    "org.lambico.datatest.example1.model.Entity1": [
        {
            "@id" : "Entity1-1",
            "id" : null,
            "stringField" : "test1",
            "entity2" : {
                "@ref" : "Entity2-1"
            }
        },
        {
            "@id" : "Entity1-2",
            "id" : null,
            "stringField" : "test3",
            "entity2" : {
                "@ref" : "Entity2-1"
            }
        }
    ],
    "org.lambico.datatest.example1.model.Entity2": [
        {
            "@id" : "Entity2-1",
            "id" : null,
            "stringField" : "test2",
            "entity1" : {
                "@ref" : "Entity1-1"
            }
        }
    ]
}
```
