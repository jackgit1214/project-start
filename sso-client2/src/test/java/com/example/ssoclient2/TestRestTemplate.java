package com.example.ssoclient2;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
public class TestRestTemplate {

    String url = "http://fuwu.rsj.beijing.gov.cn/jf2021integralpublic/settlePerson/tablePage";

    //{id:id}
    String detailUrl = "http://fuwu.rsj.beijing.gov.cn/jf2021integralpublic/settlePerson/settlePersonDetails";
    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void test() {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://www.sohu.com", String.class);

        for (int i=1;i<=10;i++){
            this.testGetScore();
        }


        String body = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();

        System.out.println("body:" + body);
        System.out.println("statusCode:" + statusCode);
        System.out.println("statusCodeValue" + statusCodeValue);
        System.out.println("headers" + headers);
    }

    @Test
    public void testPost() throws InterruptedException {

        StringBuffer tableEle = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        List<PersonInfo> persons = new ArrayList<>();
        for (int i=1;i<=61;i++){
            MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
            param.put("name", Collections.singletonList(""));
            param.put("rows", Collections.singletonList("100"));
            String page = String.valueOf((i-1)*100);
            param.put("page", Collections.singletonList(page));
            ResponseEntity<String> response =  restTemplate.postForEntity(url,param,String.class);

            String body = response.getBody();

            Document doc = Jsoup.parse(body.toString());
            Elements elements = doc.getElementsByTag("tr");
            for (Element ele:elements){
                if (!ele.getElementsByTag("th").isEmpty())
                    continue;

                PersonInfo personInfo = this.genPersonInfo(ele);
                persons.add(personInfo);
                this.getPerScore(personInfo);
                tableEle.append(personInfo.getPersonInfo());
            }
        }



//        persons.forEach(personInfo -> {
//            this.getPerScore(personInfo);
//            System.out.println(personInfo.getPersonInfo());
//        });
        System.out.println(tableEle.toString());
//        HttpStatus statusCode = response.getStatusCode();
//        int statusCodeValue = response.getStatusCodeValue();
//        HttpHeaders headers = response.getHeaders();
//          System.out.println("body:" + tableEle.toString());
//        System.out.println("statusCode:" + statusCode);
//        System.out.println("statusCodeValue" + statusCodeValue);
//        System.out.println("headers" + headers);
    }

    private PersonInfo genPersonInfo(Element element){
        PersonInfo personInfo = new PersonInfo();
        Elements elements = element.getElementsByTag("td");

        personInfo.setPerOrder(elements.get(0).text());
        personInfo.setName(elements.get(1).text());
        personInfo.setBirthday(elements.get(2).text());
        personInfo.setUnitName(elements.get(3).text());
        personInfo.setScore(elements.get(4).text());
        Element lastTd = elements.get(5).getElementsByTag("a").first();


        String detail = lastTd.attr("onclick");
        int start = detail.indexOf("(");
        int end = detail.indexOf("')");

        String perNo = detail.substring(start+2,end);
        personInfo.setId(perNo);
       // this.getPerScore(personInfo,perNo);
        return personInfo;
    }

    public void getPerScore(PersonInfo personInfo){
        Document doc = Jsoup.parse(this.getPerScore(personInfo.getId()));
        Element eleTable = doc.getElementsByClass("blue_table1").get(0);

        Elements trs = eleTable.getElementsByTag("tr");
        personInfo.setStableJob(trs.get(1).getElementsByTag("td").get(2).text());
        personInfo.setStableHome(trs.get(2).getElementsByTag("td").get(2).text());
        personInfo.setEducation(trs.get(3).getElementsByTag("td").get(2).text());
        personInfo.setEduCut(trs.get(4).getElementsByTag("td").get(2).text());
        personInfo.setLivingArea(trs.get(5).getElementsByTag("td").get(2).text());
        personInfo.setStartBusiness(trs.get(6).getElementsByTag("td").get(2).text());
        personInfo.setTaxes(trs.get(7).getElementsByTag("td").get(2).text());
        personInfo.setAge(trs.get(8).getElementsByTag("td").get(2).text());
        personInfo.setHonor(trs.get(9).getElementsByTag("td").get(2).text());
        personInfo.setKeepLaw(trs.get(10).getElementsByTag("td").get(2).text());
    }

    public String getPerScore(String id){

        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.put("id", Collections.singletonList(id));
        //取详细信息
        ResponseEntity<String> detailRes =  restTemplate.postForEntity(detailUrl,param,String.class);

        String body = detailRes.getBody();

        return body;
    }


    @Test
    public void testGetScore(){
        String body = this.getPerScore("122862");
        System.out.println(body);
    }

    @Test
    public void testOutPut(){
        String kk = "adklasjdfa sdf \r\n lasdjfal;sdfjadsf \r\n lsdfjafjasdfafasd f\r\n ";
        System.out.println(kk);
    }


    @Data
    public class PersonInfo {
        private String id ;
        private String perOrder;
        private String name;
        private String birthday;
        private String unitName;
        private String score;
        private String stableJob;
        private String stableHome;
        private String education;
        private String eduCut;
        private String livingArea;
        private String startBusiness;
        private String taxes;
        private String age;
        private String honor;
        private String keepLaw;

        public String getPersonInfo(){

            return this.perOrder+"\t"+this.name+"\t"+this.birthday+"\t"+this.unitName+"\t"+
                    this.score+"\t"+convert(this.stableJob)+"\t"+convert(this.stableHome)+"\t"+convert(this.education)+"\t"+
                    convert(this.eduCut)+"\t"+convert(this.livingArea)+"\t"+convert(this.startBusiness)+"\t"+convert(this.taxes)+"\t"+
                            convert(this.age)+"\t"+convert(this.honor)+"\t"+convert(this.keepLaw)+"\t"+convert(this.id)+"   \r\n";
        }

        private String convert(String s){
            if (StringUtils.hasText(s))
                return s;
            else
                return "";
        }
    }
}
