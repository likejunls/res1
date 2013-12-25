package com.hoo.test;
 
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.hoo.entity.AccountBean;
 
/**
 * <b>function:</b>Jackson 将java对象转换成JSON字符串，也可以将JSON字符串转换成java对象
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class JacksonTest {
    private JsonGenerator jsonGenerator = null;
    private ObjectMapper objectMapper = null;
    private AccountBean bean = null;
    
    @Before
    public void init() {
        bean = new AccountBean();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1);
        bean.setName("hoojo");
        
        objectMapper = new ObjectMapper();
        try {
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void destory() {
        try {
            if (jsonGenerator != null) {
                jsonGenerator.flush();
            }
            if (!jsonGenerator.isClosed()) {
                jsonGenerator.close();
            }
            jsonGenerator = null;
            objectMapper = null;
            bean = null;
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * Java对象转换成JSON
     * 
     */
    
    //JavaBean(Entity/Model)转换成JSON 
    @Test
    public void writeEntityJSON() {
        
        try {
            System.out.println("jsonGenerator");
            //writeObject可以转换java对象，eg:JavaBean/Map/List/Array等
            jsonGenerator.writeObject(bean);    
            System.out.println();
            
            System.out.println("ObjectMapper");
            //writeValue具有和writeObject相同的功能
            objectMapper.writeValue(System.out, bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //将Map集合转换成Json字符串 
    @Test
    public void writeMapJSON() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", bean.getName());
            map.put("account", bean);
            bean = new AccountBean();
            bean.setAddress("china-Beijin");
            bean.setEmail("hoojo@qq.com");
            map.put("account2", bean);
            
            System.out.println("jsonGenerator");
            jsonGenerator.writeObject(map);
            System.out.println("");
            
            System.out.println("objectMapper");
            objectMapper.writeValue(System.out, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //将List集合转换成json 
    @Test
    public void writeListJSON() {
        try {
            List<AccountBean> list = new ArrayList<AccountBean>();
            list.add(bean);
            
            bean = new AccountBean();
            bean.setId(2);
            bean.setAddress("address2");
            bean.setEmail("email2");
            bean.setName("haha2");
            list.add(bean);
            
            System.out.println("jsonGenerator");
            //list转换成JSON字符串
            jsonGenerator.writeObject(list);
            System.out.println();
            System.out.println("ObjectMapper");
            //用objectMapper直接返回list转换成的JSON字符串
            System.out.println("1###" + objectMapper.writeValueAsString(list));
            System.out.print("2###");
            //objectMapper list转换成JSON字符串
            objectMapper.writeValue(System.out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Jackson内置类型转换
    @Test
    public void writeOthersJSON() {
        try {
            String[] arr = { "a", "b", "c" };
            System.out.println("jsonGenerator");
            String str = "hello world jackson!";
            //byte
            jsonGenerator.writeBinary(str.getBytes());
            //boolean
            jsonGenerator.writeBoolean(true);
            //null
            jsonGenerator.writeNull();
            //float
            jsonGenerator.writeNumber(2.2f);
            //char
            jsonGenerator.writeRaw("c");
            //String
            jsonGenerator.writeRaw(str, 5, 10);
            //String
            jsonGenerator.writeRawValue(str, 5, 5);
            //String
            jsonGenerator.writeString(str);
            jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
            System.out.println();
            
            //Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectFieldStart("user");//user:{
            jsonGenerator.writeStringField("name", "jackson");//name:jackson
            jsonGenerator.writeBooleanField("sex", true);//sex:true
            jsonGenerator.writeNumberField("age", 22);//age:22
            jsonGenerator.writeEndObject();//}
            
            jsonGenerator.writeArrayFieldStart("infos");//infos:[
            jsonGenerator.writeNumber(22);//22
            jsonGenerator.writeString("this is array");//this is array
            jsonGenerator.writeEndArray();//]
            
            jsonGenerator.writeEndObject();//}
            
            
            AccountBean bean = new AccountBean();
            bean.setAddress("address");
            bean.setEmail("email");
            bean.setId(1);
            bean.setName("haha");
            //complex Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectField("user", bean);//user:{bean}
            jsonGenerator.writeObjectField("infos", arr);//infos:[array]
            jsonGenerator.writeEndObject();//}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * JSON转换成Java对象
     * 
     */
    
    //将json字符串转换成JavaBean对象
    @Test
    public void readJson2Entity() {
        String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
        try {
            AccountBean acc = objectMapper.readValue(json, AccountBean.class);
            System.out.println(acc.getName());
            System.out.println(acc);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将json字符串转换成List<Map>集合 
    @Test
    public void readJson2List() {
        String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                    "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Set<String> set = map.keySet();
                for (Iterator<String> it = set.iterator();it.hasNext();) {
                    String key = it.next();
                    System.out.println(key + ":" + map.get(key));
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Json字符串转换成Array数组
    @Test
    public void readJson2Array() {
        String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            AccountBean[] arr = objectMapper.readValue(json, AccountBean[].class);
            System.out.println(arr.length);
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }
            
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Json字符串转换成Map集合 
    @Test
    public void readJson2Map() {
        String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                    "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
        try {
            Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
            System.out.println(maps.size());
            Set<String> key = maps.keySet();
            Iterator<String> iter = key.iterator();
            while (iter.hasNext()) {
                String field = iter.next();
                System.out.println(field + ":" + maps.get(field));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Jackson对XML的支持（需要额外的jar包 stax2-api.jar）
    /**
    @Test
    public void writeObject2Xml() {
        System.out.println("XmlMapper");
        XmlMapper xml = new XmlMapper();
        
        try {
            //javaBean转换成xml
            //xml.writeValue(System.out, bean);
            StringWriter sw = new StringWriter();
            xml.writeValue(sw, bean);
            System.out.println(sw.toString());
            //List转换成xml
            List<AccountBean> list = new ArrayList<AccountBean>();
            list.add(bean);
            list.add(bean);
            System.out.println(xml.writeValueAsString(list));
            
            //Map转换xml文档
            Map<String, AccountBean> map = new HashMap<String, AccountBean>();
            map.put("A", bean);
            map.put("B", bean);
            System.out.println(xml.writeValueAsString(map));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    **/

}
