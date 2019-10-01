import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Vector<String>, Integer> time_table = ArrayListMultimap.create();

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
        //student.addElement("", new Vector<Vector<String>>(5, Vector<String>(4, "")));
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

            //System.out.printf("%s, %s\n", tea.get(0), tea.get(1));
            time_table.put(tea , time);
            //System.out.println(time_table.size());
            tea.clear();
        }

        System.out.println();

        System.out.println(time_table);

        //student.addElement("", new Vector<Vector<String>>(5, new Vector<String> (4)));
        //student.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반
        //teacher.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반

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

class student_table {
    private Integer cnt = 0;
    private Integer knt = 0;

    public boolean createTimeTable(Multimap<Vector<String>, Integer> time_table, Integer full_time, boolean overlap, Integer period, Integer day, Vector<Vector<Vector<String>>> student, Vector<Vector<Vector<String>>> teacher, Vector<Integer> arr, Integer grade) {
        Vector<String> fir = new Vector<>(5);
        Vector<Vector<String>> stu = new Vector<>(10), tea = new Vector<>(10);

        Integer n = 0, pc = 0;
        Integer pre_num = 0;

        fir.setSize(5);
        fir.add(5, "");

        boolean success = false;

        overlap = false;

        for (Integer i = 0; i < full_time * 50; i++) {
            if (time_table.isEmpty()) {
               break;
            }

            knt++;
            pc++;

            if (pc > full_time) {
                Iterator<Vector<String>> it = time_table.keySet().iterator();

                while (it.hasNext()){
                    Vector<String> s;

                    if (it.hasNext()) {
                        s = it.next();

                        for (Integer j = 0; j <= period; j++) {
                            if (s.firstElement() != stu.elementAt(j).elementAt(day) && s.lastElement() != tea.elementAt(j).elementAt(day)) {
                                overlap = true;
                            }
                        }
                    }
                }
            }

            Random random = new Random();

            //System.out.printf("%d", time_table.size());
            int number = random.nextInt(time_table.size());
            //int number = getRandomNumber(0, time_table.size() - 1);

            Iterator<Vector<String>> iter1;
            Iterator<Integer> iter2;
            iter1 = time_table.keys().iterator();
            iter2 = time_table.values().iterator();

            while (number > 0) {
                if (iter1.hasNext() || iter2.hasNext()) {
                    iter1.next();
                    iter2.next();
                }

                number--;
            }

            Vector<String> iter_first = (Vector<String>) iter1.next();
            Integer iter_second = iter2.next();

            if (period != 0 && day != 0) {
                for (int k = 0; k <= period; k++) {
                    if (iter_first.firstElement().equals(stu.elementAt(k).elementAt(day))) {
                        overlap = true;
                    }
                }

                for (int l = 0; l < grade; l++) {
                    if (iter_first.lastElement().equals(teacher.elementAt(period).elementAt(day).elementAt(l))) {
                        overlap = true;
                    }
                }
            }

            if (!overlap) {
                if (!(iter_second > 1 && period + iter_second > 4 && period <= 3)) {
                    if (!(period + iter_second > arr.elementAt(day))) {
                        for (int m = 0; m < iter_second; m++) {
                            //stu.elementAt(period).elementAt(day) = iter_first.firstElement();
                            //tea.elementAt(period).elementAt(day) = iter_first.lastElement();
                            tea.elementAt(period).elementAt(day).concat(iter_first.lastElement());
                            stu.elementAt(period).elementAt(day).concat(iter_first.firstElement());


                            period++;
                        }

                        time_table.remove(iter1, iter2);
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
