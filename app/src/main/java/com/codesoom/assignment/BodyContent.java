package com.codesoom.assignment;

public class BodyContent {
    private String content;

    public BodyContent(String content) {
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

        this.content =  sb.toString();
    }

    public String getContent() {
        return content;
    }
}
