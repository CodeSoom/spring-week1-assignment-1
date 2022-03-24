package com.codesoom.assignment;

public class Resource {
    private String path;
    private String content;

    public Resource(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public static String makeBodyContent(String content) {
        if(content.equals("")) {
            return content;
        }

        content = content.substring(11);
        content = content.substring(0, content.length() - 2);
        // 유니코드를 한글로 변환할 버퍼 선언
        StringBuffer sb = new StringBuffer();
        // 글자를 하나하나 탐색하면서 유니코드만 버퍼에 담는다.
        for (int i = 0; i < content.length(); i++) {
            // 조합이 u로 시작하면 6글자를 변환한다.
            if ('\\' == content.charAt(i) && 'u' == content.charAt(i + 1)) {
                // 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
                Character r = (char) Integer.parseInt(content.substring(i + 2, i + 6), 16);
                // 유니코드에서 한글로 변환된 글자를 버퍼에 넣는다.
                sb.append(r);
                // for의 증가 값 1과 5를 합해 6글자를 점프
                i += 5;
            }
        }

        String temp = sb.toString();
        //영문이나 숫자이면 그대로 반환
        if(temp.equals("")) {
            return content;
        }

        //유니코드를 한글로 변환한 결과를 반환
        return temp;
    }
}
