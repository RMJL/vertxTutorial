package com.rui.udemy.vertx_starter.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectExample {

  @Test
  void JsonObjectCanBeMapped() {
    JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1);
    myJsonObject.put("name", "Alice");
    myJsonObject.put("loves_vertx", true);

    final String encode = myJsonObject.encode();
    assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}", encode);

    JsonObject decodedJsonObject = new JsonObject(encode);
    assertEquals(decodedJsonObject,myJsonObject);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap() {
    final Map<String, Object> myMap = new HashMap<>();
    myMap.put("id", 1);
    myMap.put("name", "Alice");
    myMap.put("loves_vertx", true);
    final JsonObject myJsonObject = new JsonObject(myMap);

    assertEquals(myMap, myJsonObject.getMap());
    assertEquals(1, myJsonObject.getInteger("id"));
    assertEquals("Alice", myJsonObject.getString("name"));
    assertEquals(true, myJsonObject.getBoolean("loves_vertx"));
  }

  @Test
  void jsonArrayCanBeMapped() {
    JsonArray myJsonArray = new JsonArray();
    myJsonArray
      .add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3))
      .add("randomValue");

    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomValue\"]", myJsonArray.encode());
  }

  @Test
  void canMapJavaObjects() {
    Person person = new Person(1, "Alice", true);
    JsonObject alice = JsonObject.mapFrom(person);

    assertEquals(person.getId(), alice.getInteger("id"));
    assertEquals(person.getName(), alice.getString("name"));
    assertEquals(person.isLovesVertx(), alice.getBoolean("lovesVertx"));

    Person convertedPerson = alice.mapTo(Person.class);
    assertEquals(person.getId(), convertedPerson.getId());
    assertEquals(person.getName(), convertedPerson.getName());
    assertEquals(person.isLovesVertx(), convertedPerson.isLovesVertx());
  }
}
