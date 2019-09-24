import java.util.Scanner;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;
import com.google.common.collect.Multimap;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Vector<String>, Integer> time_table = null;
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

            if (time_table != null) {
                time_table.put(tea, time);
            }

            tea.clear();
        }

        System.out.println();

        //student.addElement("", new Vector<Vector<String>>(5, new Vector<String> (4)));
        //student.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반
        //teacher.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반

        studentable set_table = new studentable();

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

class studentable {
    private Integer cnt = 0;
    private Integer knt = 0;

    public boolean createTimeTable(Multimap<Vector<String>, Integer> time_table, Integer full_time, boolean overlap, Integer period, Integer day, Vector<Vector<Vector<String>>> student, Vector<Vector<Vector<String>>> teacher, Vector<Integer> arr, Integer grade) {
        Vector<Vector<String>> stu = new Vector<>(), tea = new Vector<>();

        Integer n = 0, pc = 0;
        Integer pre_num = 0;

        stu.clear();
        //stu.assign(10, vector < string > (5, ""));

        tea.clear();
        //tea.assign(10, vector < string > (5, ""));

        boolean success = false;

        overlap = false;

        for (Integer i = 0; i < full_time * 50; i++) {
            //if (time_table.isEmpty()) {
            //   break;
            //}

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

            System.out.printf("%d", time_table.size());
            int number = random.nextInt(time_table.size());
            //int number = getRandomNumber(0, time_table.size() - 1);

            Iterator<Vector<String>> iter1;
            Iterator<Integer> iter2;
            iter1 = time_table.keySet().iterator();
            iter2 = time_table.values().iterator();

            while (number-- > 0) {
                if (iter1.hasNext() || iter2.hasNext()) {
                    iter1.next();
                    iter2.next();
                }
            }

            Vector<String> iter_first = iter1.next();
            Integer iter_second = iter2.next();

            for (int k = 0; k <= period; k++) {
                int finalK = k;
                Integer finalDay = day;
                if (iter_first.firstElement() == stu.elementAt(finalK).elementAt(finalDay)) {
                    overlap = true;
                }
            }

            for (int l = 0; l < grade; l++) {
                if (iter_first.lastElement() == teacher.elementAt(period).elementAt(day).elementAt(l)) {
                    overlap = true;
                }
            }

            if (!overlap) {
                if (!(iter_second.intValue() > 1 && period + iter_second.intValue() > 4 && period <= 3)) {
                    if (!(period + iter_second.intValue() > arr.elementAt(day))) {
                        for (int m = 0; m < iter_second.intValue(); m++) {
                            //stu.elementAt(period).elementAt(day) = iter_first.firstElement();
                            //tea.elementAt(period).elementAt(day) = iter_first.lastElement();

                            stu.elementAt(period).elementAt(day).concat(iter_first.firstElement());
                            tea.elementAt(period).elementAt(day).concat(iter_first.lastElement());

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