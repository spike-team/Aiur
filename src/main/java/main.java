import java.util.*;

import com.google.common.collect.*;
import org.checkerframework.checker.nullness.qual.Nullable;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Integer, Table> time_table = ArrayListMultimap.create();

        int[] dy = {0, 0, 0, 0, 0};
        int[] arr = {7, 7, 7, 7, 6};

        Vector<Vector<Vector<String>>> student = new Vector<>();
        Vector<Vector<Vector<String>>> teacher = new Vector<>();

        String subject, name;
        Integer time, full_time = 28, day = 0, period = 0;

        Vector<Vector<String>> tea = new Vector<Vector<String>>(28);

        //tea.add(2, new Vector<>(Collections.singleton("")));

        for (int i = 0; i < full_time; i++) {
            Vector<String> sub = new Vector<>();

            subject = sc.next();
            name = sc.next();
            time = sc.nextInt();

            sub.add(subject);
            sub.add(name);

            tea.add(sub);

            time_table.put(i, new Table(tea.elementAt(i), time));
        }

        System.out.println();

        student_table set_table = new student_table();

        Integer grade = 0;
        while (grade < 1) {
            if
            (!set_table.createTimeTable(time_table, full_time, period, day, student, teacher, arr, grade)) {
                grade++;
            }
        }
        /*
        for (int k = 0; k < grade; k++) {
            for (int i = 0; i < student.size(); i++) {
                for (int j = 0; j < student.elementAt(i).size(); j++) {
                    System.out.printf("%s", student.elementAt(i).elementAt(j).elementAt(k));
                }
                System.out.println();
            }
        }
        */
        //System.out.println(knt + " " + cnt);
    }
}

class Table {
    Vector<String> cource;
    Integer time;

    Table() {
        cource = new Vector<>();
        time = 0;
    }

    Table(Vector<String> cur, Integer tm) {
        cource = cur;
        time = tm;
    }
}

class student_table {
    private Integer cnt = 0;
    private Integer knt = 0;

    public boolean createTimeTable(Multimap<Integer, Table> time_table, Integer full_time, Integer periot, Integer dat, Vector<Vector<Vector<String>>> student, Vector<Vector<Vector<String>>> teacher, int[] arr, Integer grade) {
        Multimap<Integer, Table> time_table_copy = ArrayListMultimap.create();

        Iterator<Integer> tableInteger = time_table.keys().iterator();
        Iterator<Table> tableIterator = time_table.values().iterator();

        while (tableIterator.hasNext() && tableIterator.hasNext()) {
            Integer k = tableInteger.next();
            Table v = tableIterator.next();

            time_table_copy.put(k, v);
        }

        String[][] stu = new String[10][5];
        String[][] tea = new String[10][5];

        Integer day = dat;
        Integer period = periot;

        Integer pc = 0;
        boolean overlap = false;

        for (Integer i = 0; i < full_time * 50; i++) {
            if (time_table_copy.size() == 0) {
                break;
            }

            knt++;
            pc++;

            if (pc > full_time) {
                Iterator<Table> it = time_table_copy.values().iterator();

                while (it.hasNext()) {
                    if (it.hasNext()) {
                        Table table = it.next();

                        for (Integer j = 0; j <= period; j++) {
                            if (table.cource.firstElement() != stu[j][day] && table.cource.lastElement() != tea[j][day]) {
                                overlap = true;
                                break;
                            }
                        }
                    }
                }
            }

            Random random = new Random();

            int number = random.nextInt(time_table.size());

            Iterator<Integer> iter_key;
            Iterator<Table> iter;

            iter_key = time_table_copy.keys().iterator();
            iter = time_table_copy.values().iterator();

            Integer key = 0;
            Table table = new Table();

            while (number >= 0) {
                if (iter.hasNext() || iter_key.hasNext()) {
                    key = iter_key.next();
                    table = iter.next();
                }

                number--;
            }

            if (day != 0 && period != 0) {
                for (int k = 0; k <= period; k++) {
                    if (table.cource.firstElement().equals(stu[k][day])) {
                        overlap = true;
                        break;
                    }
                }

                for (int l = 0; l < grade; l++) {
                    if (table.cource.firstElement().equals(teacher.elementAt(period).elementAt(day).elementAt(l))) {
                        overlap = true;
                        break;
                    }
                }
            }

            if (!overlap) {
                if (!(table.time > 1 && period + table.time > 4 && period <= 3)) {
                    if (!(period + table.time > arr[day])) {
                        for (int m = 0; m < table.time; m++) {
                            String str1 = table.cource.firstElement();
                            String str2 = table.cource.lastElement();

                            stu[period][day] = str1;
                            tea[period][day] = str2;

                            System.out.print(str1 + " ");
                            System.out.println(str2);
                            period++;

                        }

                        time_table_copy.remove(key, table);
                        pc = 0;
                    }
                }
            }

            //if (stu[period][day] != "")
            //	period++;

            if (period == arr[day]) {
                System.out.println("day : " + (day + 1) + "\nperiod : " + period);

                day++;
                period = 0;
            }
        }

        if (time_table_copy.isEmpty()) {
            System.out.println("size 0 out!");
            for (int i = 0; i < stu.length; i++) {
                for (int j = 0; j < stu[i].length; j++) {
                    System.out.print(stu[i][j] + " ");
                }
                System.out.println();
            }

            return false;
        }

        cnt++;

        System.out.println();
        return true;
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
