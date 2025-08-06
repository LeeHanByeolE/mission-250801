package com.back;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Repository {
    private static int No = 0;
    private final String DIR_PATH = "db/wiseSaying";

    public Repository() {
        Path directory = Path.of(DIR_PATH);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패" + e.getMessage());
            }
        }
        Path lastId = Path.of(DIR_PATH + "/lastId.txt");
        try {
            if (!Files.exists(lastId)) {
                Files.createFile(lastId);
            } else {
                if (!Files.readString(lastId).isEmpty()) {
                    No = Integer.parseInt(Files.readString(lastId));
                }
            }
        } catch (IOException e) {
            System.out.println("lastId.txt 생성 실패" + e.getMessage());
        }
    }

    public void build() {
        File[] files = Path.of(DIR_PATH).toFile().listFiles((d, name) -> name.endsWith(".json"));

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while((line = br.readLine()) != null) {
                    sb.append("  " + line);
                    if(line.contains("}") && i == files.length - 1) {
                        sb.append("\n");
                    } else if(line.contains("}")) {
                        sb.append(",\n");
                    } else{
                        sb.append("\n");
                    }
                }
            } catch(IOException e) {
                System.out.println("빌드 실패" + e.getMessage());
            }
        }
        sb.append("]");
        try (FileWriter fw = new FileWriter(DIR_PATH + "/data.json")) {
            fw.write(sb.toString());
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }catch (IOException e) {
            System.out.println("build write filed");
        }
    }

    public void modify(WiseSaying ws, Scanner kb){
        System.out.println("명언(기존) : " + ws.getWiseSaying());
        System.out.print("명언 : ");
        String content = kb.nextLine();
        System.out.println("작가(기존) : " + ws.getAuthor());
        System.out.print("작가 : ");
        String author = kb.nextLine();
        try (FileWriter fw = new FileWriter(DIR_PATH + "/" + ws.getId() + ".json")) {
            System.out.println(DIR_PATH + "/" + ws.getId() + ".json");
            fw.write("{\n" +
                    "  \"id\": " + ws.getId() + ",\n" +
                    "  \"content\": " + escape(content) + ",\n" +
                    "  \"author\": " + escape(author) + "\n" +
                    "}");
        } catch (IOException e) {
            System.out.println("modify failed" + e.getMessage());
        }
    }

    public void delete(int idx){
        Path file = Path.of(DIR_PATH + "/" + idx + ".json");
        if(!Files.exists(file)){
            System.out.println(idx + "번 명언은 존재하지 않습니다.");
            return;
        } else {
            try {
                Files.delete(file);
                System.out.println(idx + "번 명언이 삭제되었습니다.");
            } catch (IOException e) {
                System.out.println("file delete fail" + e.getMessage());
            }
        }
    }

    public void printAll(List<WiseSaying> list) {
        System.out.println("번호 / 작가 / 명언\n" +
                "----------------------");
        if(list.isEmpty()){
            System.out.println("등록된 명언이 없습니다");
            return;
        }
        list.sort(null);
        for (WiseSaying ws : list) {
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getWiseSaying());
        }
    }

    public WiseSaying write(String ws, String author) throws IOException {
        //저장
        try (FileWriter fw = new FileWriter(DIR_PATH + "/" + (++No) + ".json")) {
            fw.write("{\n" +
                    "  \"id\": " + No + ",\n" +
                    "  \"content\": " + escape(ws) + ",\n" +
                    "  \"author\": " + escape(author) + "\n" +
                    "}");
            System.out.println(No + "번 명언이 등록되었습니다.");
        }
        //lastIdx 저장
        try (FileWriter fw = new FileWriter(DIR_PATH + "/lastId.txt")) {
            fw.write(String.valueOf(No));
        }
        return new WiseSaying(No, ws, author);
    }

    public List<WiseSaying> load(){
        List<WiseSaying> list = new ArrayList<>();

        File[] files = Path.of(DIR_PATH).toFile().listFiles((d, name) -> name.endsWith(".json") && !name.equalsIgnoreCase("data.json"));

        if (files == null || files.length == 0) {
            return new ArrayList <>();
        }

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int id = 0;
                String content = null;
                String author = null;

                while ((line = br.readLine()) != null) {
                    if (line.trim().startsWith("\"id\"")) {
                        id = Integer.parseInt(line.split(":")[1].trim().replace(",", ""));
                    } else if (line.trim().startsWith("\"content\"")) {
                        content = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                    } else if (line.trim().startsWith("\"author\"")) {
                        author = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                    }
                }

                list.add(new WiseSaying(id, content, author));

            } catch (IOException e) {
                System.out.println("파일 읽기 오류: " + file.getName() + " / " + e.getMessage());
            }
        }
        return list;
    }
    private String escape(String s) {
        return "\"" + s + "\"";
    }

}
