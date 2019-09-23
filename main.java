import java.util.Scanner;
import java.util.Iterator;
import java.util.Vector;

import com.google.common.collect.Multimap;

import static java.math.BigInteger.TWO;

public class main {
    public static void main(String[] args) {
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
        String subject = "", name = "";
        Integer time = 0, full_time = 28, day = 0, period = 0, grade = 0;
        boolean overlap = false;

        for (int i = 0; i < full_time; i++) {
            subject = sc.next();
            name = sc.next();
            time = sc.nextInt();

            tea.add(subject);
            tea.add(name);

            time_table.put(tea, time);

            tea.clear();
        }

        System.out.println();

        //student.addElement("", new Vector<Vector<String>>(5, new Vector<String> (4)));
        //student.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반
        //teacher.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반

        studentable set_table = new studentable();

        while (grade != 4) { // 반 갯수
            if (!set_table.createTimeTable(time_table, full_time, overlap, period, day, student, teacher, arr, grade)) {
                grade++;
            }
        }

        // 과목 출력
        for (int k = 0; k < grade; k++) {
            for (int i = 0; i < student.size(); i++) {
                for (int j = 0; j < student[i].size(); j++) {
                    System.out.printf("%s", student[i][j][k]);
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

    /*
    int getRandomNumber(int min, int max) {
        //< 1단계. 시드 설정
        random_device rn;
        mt19937_64 rnd (rn());

        //< 2단계. 분포 설정 ( 정수 )
        uniform_int_distribution<int> range (min, max);

        //< 3단계. 값 추출
        return range(rnd);
    }
    */

    public boolean createTimeTable(Multimap<Vector<String>, Integer> time_table, Integer full_time, boolean overlap, Integer period, Integer day, Vector<Vector<Vector<String>>> student, Vector<Vector<Vector<String>>> teacher, Vector<Integer> arr, Integer grade) {
        //Multimap<Vector<String>, Integer>::Iterator iter;
        Iterator<Vector<String>> iter;
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
            if (time_table.size() == 0)
                break;

            knt++;
            pc++;

            if (pc > full_time) {
                Iterator<Vector<String>> it = time_table.keySet().iterator();

                while (it.hasNext()){
                    Vector<String> s = it.next();

                    for (Integer j = 0; j <= period; j++) {
                        if (s.firstElement() != stu[j][day] && s.lastElement() != tea[j][day])
                            overlap = true;
                    }
                }

                /*
                for (Iterator<Vector<String>> it = time_table.begin(); it != time_table.; it++) {
                    boolean cheak = false;
                    for (Integer j = 0; j <= period; j++) {
                        if (it -> first.front() != stu[j][day] && it -> first.back() != tea[j][day])
                            cheak = true;

                        if (cheak)
						    goto TWO;
                    }
                }
                */
            }

            int number = getRandomNumber(0, time_table.size() - 1);
            //iter = next(time_table.begin(), number);
            iter = time_table.keySet().iterator();

            for (int k = 0; k <= period; k++) {
                if (iter -> first.front() == stu[k][day]) {
                //    goto ONE;
                    overlap = true;
                }
            }

            for (int l = 0; l < grade; l++) {
                if (iter -> first.back() == teacher[period][day][l]) {
				 //   goto ONE;
                    overlap = true;
                }
            }

            if (!overlap) {
                if (!(iter -> second > 1 && period + iter -> second > 4 && period <= 3)) {
                    if (!(period + iter -> second > arr[day])) {
                        for (int m = 0; m < iter -> second; m++) {
                            stu[period][day] = iter -> first.front();
                            tea[period][day] = iter -> first.back();

                            period++;
                        }

                        time_table.erase(iter);
                        pc = 0;
                    }
                }
            }

            //if (stu[period][day] != "")
            //	period++;

            if (period == arr[day]) {
                day++;
                period = 0;
            }
        }

        if (time_table.size() == 0) {
            for (int i = 0; i < stu.size(); i++) {
                for (int j = 0; j < stu[i].size(); j++) {
                    if (stu[i][j] != "")
                        student[i][j][grade] = stu[i][j];
                    if (tea[i][j] != "")
                        teacher[i][j][grade] = tea[i][j];
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