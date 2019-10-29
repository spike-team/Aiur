import java.security.KeyPair;
import java.util.Scanner;
import java.util.Map;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.Collection;
import com.google.common.collect.Multimap;

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Vector<String>, Integer> time_table;
        Vector<String> tea;
        Vector<Integer> dy = {0, 0, 0, 0, 0};
        Vector<Integer> arr = {7, 7, 7, 7, 6}; // 월, 화, 수, 목, 금 각각 총 교시
        Vector<Vector<Vector<String>>> student, teacher;
        String subject = "", name = "";
        Integer time = 0, full_time = 28, day = 0, period = 0, grade = 0;
        boolean overlap = false;

        for (int i = 0; i < full_time; i++) {
            subject = sc.next();
            name = sc.next();
            time = sc.nextInt();
            //cin >> subject >> name >> time;

            tea.add(subject);
            tea.add(name);
            //tea.push_back(subject);
            //tea.push_back(name);

            time_table.put(tea, time);
            //time_table.insert(pair < Vector < String >, Integer>(tea, time));

            tea.clear();
        }

        System.out.println();

        student.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반
        teacher.assign(10, Vector < Vector < String >> (5, Vector < String > (4, ""))); // 10교시, 5일, 4반

        while (grade != 4) { // 반 갯수
            if (!createTimeTable(time_table, full_time, overlap, period, day, student, teacher, arr, grade)) {
                grade++;
            }
        }

        // 과목 출력
        for (int k = 0; k < grade; k++) {
            for (int i = 0; i < student.size(); i++) {
                for (int j = 0; j < student[i].size(); j++) {
                    System.out.printf("%s", student[i][j][k]);

                    cout << student[i][j][k] << " ";
                }
                System.out.println();
            }
        }

	/* // 선생님 출력
	for (int k = 0; k < grade; k++) {
		for (int i = 0; i < teacher.size(); i++) {
			for (int j = 0; j < teacher[i].size(); j++) {
				cout << teacher[i][j][k] << " ";
			}
			cout << endl;
		}
	}
	*/
        cout << knt << " " << cnt << endl;
    }
}

class studentable {
    int getRandomNumber(int min, int max) {
        //< 1단계. 시드 설정
        random_device rn;
        mt19937_64 rnd (rn());

        //< 2단계. 분포 설정 ( 정수 )
        uniform_int_distribution<int> range (min, max);

        //< 3단계. 값 추출
        return range(rnd);
    }

    bool createTimeTable(multimap<vector<string>, int> time_table, int full_time, bool overlap, int period, int day, vector<vector<vector<string>>> &student, vector<vector<vector<string>>> &teacher, vector<int> arr, int grade) {
        multimap<vector<string>, int>::iterator iter;
        vector<vector<string>> stu, tea;

        int n = 0, pc = 0;
        int pre_num = 0;

        stu.clear();
        stu.assign(10, vector < string > (5, ""));

        tea.clear();
        tea.assign(10, vector < string > (5, ""));

        bool success = false;

        overlap = false;

        for (int i = 0; i < full_time * 50; i++) {
            if (time_table.size() == 0)
                break;

            ONE:
            knt++;
            pc++;

            if (pc > full_time) {
                for (multimap<vector<string>, int>::iterator it = time_table.begin();
                it != time_table.end();
                it++){
                    bool cheak = false;
                    for (int i = 0; i <= period; i++) {
                        if (it -> first.front() != stu[i][day] && it -> first.back() != tea[i][day]) {
                            cheak = true;
                        }

                        if (cheak) {
						goto TWO;
                        }
                    }
                }
            }

            int number = getRandomNumber(0, time_table.size() - 1);
            iter = next(time_table.begin(), number);

            for (int i = 0; i <= period; i++) {
                if (iter -> first.front() == stu[i][day]) {
				goto ONE;
                }
            }

            for (int i = 0; i < grade; i++) {
                if (iter -> first.back() == teacher[period][day][i]) {
				goto ONE;
                }
            }

            if (!overlap) {
                if (!(iter -> second > 1 && period + iter -> second > 4 && period <= 3)) {
                    if (!(period + iter -> second > arr[day])) {
                        for (int i = 0; i < iter -> second; i++) {
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

        TWO:
        cnt++;

        return true;
    }
}