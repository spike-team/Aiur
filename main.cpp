#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <random>

using namespace std;

int cnt = 0, knt = 0;

int getRandomNumber(int min, int max)
{
	random_device rn;
	mt19937_64 rnd(rn());
	uniform_int_distribution<int> range(min, max);

	return range(rnd);
}

bool createTimeTable(multimap<vector<string>, int> time_table, int full_time, bool overlap, int period, int day, vector<vector<vector<string>>> &student, vector<vector<vector<string>>> &teacher, vector<int> arr, int grade) {
	multimap<vector<string>, int>::iterator iter;
	vector<vector<string>> stu, tea;

	int n = 0, pc = 0;
	int pre_num = 0;

	stu.assign(10, vector<string>(5, ""));
	tea.assign(10, vector<string>(5, ""));

	bool success = false;
	
	overlap = false;

	for (int i = 0; i < full_time * 50; i++) {
		if (time_table.size() == 0)
			break;
		
	ONE: 
		knt++;
		pc++;

		if (pc > full_time) {
			for (multimap<vector<string>, int>::iterator it = time_table.begin(); it != time_table.end(); it++) {
				bool cheak = false;

				for (int i = 0; i <= period; i++)
					if (it->first.front() != stu[i][day] && it->first.back() != tea[i][day])
						goto TWO;
			}
		}

		int number = getRandomNumber(0, time_table.size() - 1);
		iter = next(time_table.begin(), number);

		for (int i = 0; i <= period; i++)
			if (iter->first.front() == stu[i][day])
				goto ONE;
		
		for (int i = 0; i < grade; i++)
			if (iter->first.back() == teacher[period][day][i])
				goto ONE;
		
		if (!overlap) {
			if (!(iter->second > 1 && period + iter->second > 4 && period <= 3)) {
				if (!(period + iter->second > arr[day])) {
					for (int i = 0; i < iter->second; i++) {
						stu[period][day] = iter->first.front();
						tea[period][day] = iter->first.back();

						period++;
					}
					
					time_table.erase(iter);
					pc = 0;
				}
			}
		}
		
		if (stu[period][day] != "")
			period++;
		
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

int main() {
	ios::sync_with_stdio(false);
	cin.tie(NULL);
	cout.tie(NULL);
	
	multimap<vector<string>, int> time_table;
	vector<string> tea;
	vector<int> dy = { 0, 0, 0, 0, 0 };
	vector<int> arr = { 7, 7, 7, 7, 6 };
	vector<vector<vector<string>>> student, teacher;
	string subject = "", name = "";
	int time = 0, full_time = 28, day = 0, period = 0, grade = 0;
	bool overlap = false;;

	for (int i = 0; i < full_time; i++) {
		cin >> subject >> name >> time;

		tea.push_back(subject);
		tea.push_back(name);

		time_table.insert(pair<vector<string>, int>(tea, time));

		tea.clear();
	}

	cout << endl;
 
	student.assign(10, vector<vector<string>>(5, vector<string>(2, "")));
	teacher.assign(10, vector<vector<string>>(5, vector<string>(2, "")));

	while (grade != 2)
		if (!createTimeTable(time_table, full_time, overlap, period, day, student, teacher, arr, grade)) grade++;

	for (int k = 0; k < grade; k++) {
		for (int i = 0; i < student.size(); i++) {
			for (int j = 0; j < student[i].size(); j++) {
				cout << student[i][j][k] << " ";
			}
			cout << endl;
		}
	}

	cout << knt << " " << cnt << endl;

	return 0;
}