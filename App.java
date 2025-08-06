package com.back;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    Scanner kb;
    Repository repository;
    List<WiseSaying> list;

    public App() {
        kb= new Scanner(System.in);
        repository = new Repository();
        list = repository.load();
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = kb.nextLine();

            if (command.equals("종료")) {
                break;
            } else if (command.equals("등록")) {
                actionWrite();
            } else if (command.equals("목록")) {
                actionList();
            } else if (command.contains("삭제")) {
                actionDelete(command);
            } else if (command.contains("수정")) {
                actionModify(command);
            } else if (command.contains("빌드")) {
                actionBuild();
            }
        }
    }

    private void actionBuild() {
        repository.build();
    }

    private void actionModify(String command) {
        int idx = Integer.parseInt(command.split("=")[1]);
        WiseSaying ws = findWiseSaying(idx);
        if (ws == null) {
            System.out.println(idx + "번 명언은 존재하지 않습니다.");
        } else {
            repository.modify(ws, kb);
            list = repository.load();
        }
    }

    public WiseSaying findWiseSaying(int idx) {
        for (WiseSaying wiseSaying : list) {
            if(wiseSaying.getId() == idx) {
                return wiseSaying;
            }
        }
        return null;
    }

    private void actionDelete(String command) {
        repository.delete(Integer.parseInt(command.split("=")[1]));
        list = repository.load();
    }

    private void actionList() {
        repository.printAll(list);
    }

    private void actionWrite() {
        System.out.print("명언 : ");
        String ws = kb.nextLine();
        System.out.print("작가 : ");
        String author = kb.nextLine();

        try {
            WiseSaying wiseSaying = repository.write(ws, author);
            list.add(wiseSaying);
        } catch (IOException e) {
            System.out.println("actionWrite .write error : " + e.getMessage());
        }
    }
}
