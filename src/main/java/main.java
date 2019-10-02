import java.util.*;

import com.google.common.collect.*;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Integer, Table> time_table = ArrayListMultimap.create();

        Vector<String> tea = new Vector<>();
        Vector<Integer> dy = new Vector<Integer>(); // = { 0, 0, 0, 0, 0 }; // 알고리즘 제작 시작 교시
        Vector<Integer> arr = new Vector<>(); // = { 7, 7, 7, 7, 6 }; // 월, 화, 수, 목, 금 각각 총 교시

        for (int i = 0; i < 5; i++) {
            dy.addElement(0);

            if (i != 4)
                arr.addElement(7);
            else
                arr.addElement(6);
        }

        Vector<Vector<Vector<String>>> student = new Vector<Vector<Vector<String>>>();
        Vector<Vector<Vector<String>>> teacher = new Vector<Vector<Vector<String>>>();
        String subject, name;
        Integer time, full_time = 28, day = 0, period = 0;
        boolean overlap = false;

        for (int i = 0; i < full_time; i++) {
            subject = sc.next();
            name = sc.next();
            time = sc.nextInt();

            tea.addElement(subject);
            tea.addElement(name);

            time_table.put(i , new Table(tea, time));
            tea.clear();
        }

        System.out.println();
        //System.out.println(time_table);

        student_table set_table = new student_table();

        Integer grade = 0;
        while (grade != 4) { // 반 갯수
            if (!set_table.createTimeTable(time_table, full_time, overlap, period, day, student, teacher, arr, grade)) {
                grade++;
            }
        }

        // 과목 출력
        for (int k = 0; k < grade; k++) {
            for (int i = 0; i < student.size(); i++) {
                for (int j = 0; j < student.elementAt(i).size(); j++) {
                    System.out.printf("%s", student.elementAt(i).elementAt(j).elementAt(k));
                    //cout << student[i][j][k] << " ";
                }
                System.out.println();
            }
        }

        System.out.println(knt + " " + cnt);
    }
}

class Table {
    Vector<String> cource;
    Integer time;

    Table () {
        cource = new Vector<>();
        time = 0;
    }

    Table (Vector<String> cur, Integer tm) {
        cource = cur;
        time = tm;
    }
}

class student_table {
    private Integer cnt = 0;
    private Integer knt = 0;

    public boolean createTimeTable(Multimap<Integer, Table> time_table, Integer full_time, boolean overlap, Integer period, Integer day, Vector<Vector<Vector<String>>> student, Vector<Vector<Vector<String>>> teacher, Vector<Integer> arr, Integer grade) {
        Vector<Vector<String>> stu = new Vector<>(), tea = new Vector<>();

        Integer n = 0, pc = 0;
        Integer pre_num = 0;
        boolean success = false;

        overlap = false;

        for (Integer i = 0; i < full_time * 50; i++) {
            if (time_table.isEmpty()) {
               break;
            }

            knt++;
            pc++;

            if (pc > full_time) {
                Iterator<Table> it = time_table.values().iterator();

                while (it.hasNext()) {
                    if (it.hasNext()) {
                        Table table = it.next();

                        for (Integer j = 0; j <= period; j++) {
                            if (table.cource.firstElement() != stu.elementAt(j).elementAt(day) && table.cource.lastElement() != tea.elementAt(j).elementAt(day)) {
                                overlap = true;
                            }
                        }
                    }
                }
            }

            Random random = new Random();

            int number = random.nextInt(time_table.size());

            Iterator<Integer> iter_key;
            Iterator<Table> iter;

            iter_key = time_table.keys().iterator();
            iter = time_table.values().iterator();

            Integer key = 0;
            Table table = new Table();

            while (number > 0) {
                if (iter.hasNext() || iter.hasNext()) {
                    key = iter_key.next();
                    table = iter.next();
                }

                System.out.println("key : " + key);
                System.out.print("subject : " + table.cource.firstElement());
                System.out.print(", name : " + table.cource.lastElement());
                System.out.print(", time : " + table.time + "\n");


                number--;
            }

            if (period != 0 && day != 0) {
                for (int k = 0; k <= period; k++) {
                    if (table.cource.firstElement().equals(stu.elementAt(k).elementAt(day))) {
                        overlap = true;
                    }
                }

                for (int l = 0; l < grade; l++) {
                    if (table.cource.lastElement().equals(teacher.elementAt(period).elementAt(day).elementAt(l))) {
                        overlap = true;
                    }
                }
            }

            if (!overlap) {
                if (!(table.time > 1 && period + table.time > 4 && period <= 3)) {
                    if (!(period + table.time > arr.elementAt(day))) {
                        for (int m = 0; m < table.time; m++) {
                            //stu.elementAt(period).elementAt(day) = iter_first.firstElement();
                            //tea.elementAt(period).elementAt(day) = iter_first.lastElement();
                            tea.elementAt(period).elementAt(day).concat(table.cource.lastElement());
                            stu.elementAt(period).elementAt(day).concat(table.cource.firstElement());

                            period++;
                        }

                        time_table.remove(key, table);
                        pc = 0;
                    }
                }
            }

            //if (stu[period][day] != "")
            //	period++;

            if (period == arr.elementAt(day)) {
                day++;
                period = 0;
            }
        }

        if (time_table.size() == 0) {
            for (int i = 0; i < stu.size(); i++) {
                for (int j = 0; j < stu.elementAt(i).size(); j++) {
                    if (stu.elementAt(i).elementAt(j).equals(""))
                        student.elementAt(i).elementAt(j).elementAt(grade).concat(stu.elementAt(i).elementAt(j));
                    if (tea.elementAt(i).elementAt(j).equals(""))
                        teacher.elementAt(i).elementAt(j).elementAt(grade).concat(stu.elementAt(i).elementAt(j));
                }
            }

            return false;
        }

        cnt++;

        return true;
    }

    public Integer getCnt() {
        return cnt;
    }

    public Integer getKnt() {
        return knt;
    }
}

/*
창체 안현수 1
창체 임채홍 2
창체 안현수 1
디비 안현수 2
디비 안현수 1
확통 설은선 1
확통 설은선 1
확통 설은선 1
체육 장필준 1
체육 장필준 1
국사 서현철 1
국사 서현철 1
국사 서현철 1
자바 신요셉 3
자바 신요셉 2
자바 신요셉 1
논술 강래형 1
논술 강래형 1
네트 이경희 2
네트 이경희 1
영어 임채홍 1
영어 임채홍 1
영어 임채홍 1
미술 강래형 1
미술 강래형 1
문학 장보현 1
문학 장보현 1
문학 장보현 1

{ FULL_TIME 28, 7 7 7 7 6, 4반 }
*/
