package com.footbolic.api.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

@Slf4j
public class HttpUtil {

    public HttpURLConnection getConn(String urlStr, String method) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URI(urlStr).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method); //Method 방식 설정. GET/POST/DELETE/PUT/HEAD/OPTIONS/TRACE
            conn.setConnectTimeout(5000); //연결제한 시간 설정. 5초 간 연결시도
        } catch(IOException e) {
            log.error("{}", e.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public String getHttpResponse(HttpURLConnection conn) {
        StringBuilder sb = null;

        try {
            if(conn.getResponseCode() == 200) {
                // 정상적으로 데이터를 받았을 경우
                //데이터 가져오기
                sb = readResopnseData(conn.getInputStream());
            }else{
                // 정상적으로 데이터를 받지 못했을 경우
                //오류코드, 오류 메시지 표출
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                //오류정보 가져오기
                sb = readResopnseData(conn.getErrorStream());
                System.out.println("error : " + sb.toString());
                return null;
            }
        } catch (IOException e) {
            log.error("{}", e.toString());
        }finally {
            conn.disconnect(); //연결 해제
        }

        if(sb == null) return null;

        return sb.toString();
    }

    public StringBuilder readResopnseData(InputStream in) {
        if(in == null ) return null;

        StringBuilder sb = new StringBuilder();
        String line;

        try (InputStreamReader ir = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(ir)){
            while( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("{}", e.toString());
        }
        return sb;
    }
}