#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <random>

using namespace std;

int cnt = 0, knt = 0;

int getRandomNumber(int min, int max)
{
	//< 1�ܰ�. �õ� ����
	random_device rn;
	mt19937_64 rnd(rn());

	//< 2�ܰ�. ���� ���� ( ���� )
	uniform_int_distribution<int> range(min, max);

	//< 3�ܰ�. �� ����
	return range(rnd);
}

bool createTimeTable(multimap<int, vector<string>> time_table, int full_time, bool overlap, int period, int day, vector<vector<vector<string>>> &student, vector<vector<vector<string>>> &teacher, vector<int> arr, int grade) {
	multimap<int, vector<string>>::iterator iter;
	vector<vector<string>> stu, tea;

	stu.clear();
	stu.assign(10, vector<string>(5, ""));

	tea.clear();
	tea.assign(10, vector<string>(5, ""));

	bool success = false;

	for (int i = 0; i < full_time * 10; i++) {
		overlap = false;

		if (time_table.size() == 0)
			break;
		
		int number = getRandomNumber(0, time_table.size() - 1);
		iter = next(time_table.begin(), number);

		for (int i = 0; i <= period; i++)
			if (iter->second.front() == stu[i][day])
				overlap = true;
		
		for (int i = 0; i < grade; i++)
			if (iter->second.back() == teacher[period][day][i])
				overlap = true;
		
		if (!overlap) {
			if (!(iter->first > 1 && period + iter->first >= 4 && period <= 3)) {
				if (period + iter->first > arr[day]) {
					continue;
				}
				else {
					for (int i = 0; i < iter->first; i++) {
						stu[period][day] = iter->second.front();
						tea[period][day] = iter->second.back();
					
						period++;
					}
					time_table.erase(iter);
				}
			}
		}

		if (period == arr[day]) {
			day++;
			period = 0;
		}

		knt++;
	}

	if (time_table.size() == 0) {
		for (int i = 0; i < stu.size(); i++) {
			for (int j = 0; j < stu[i].size(); j++) {
				student[i][j][grade] = stu[i][j];
				teacher[i][j][grade] = tea[i][j];
			}
		}

		return false;
	}

	cnt++;

	return true;
}

int main() {
	ios::sync_with_stdio(false);
	cin.tie(NULL);
	cout.tie(NULL);
	
	multimap<int, vector<string>> time_table;
	vector<string> tea;
	vector<int> arr = { 7, 7, 7, 7, 6 }; // ��, ȭ, ��, ��, �� ���� �� ����
	vector<vector<vector<string>>> student, teacher;
	string subject = "", name = "";
	int time = 0, full_time = 28, day = 0, period = 0, grade = 0;
	bool overlap = false;;

	for (int i = 0; i < full_time; i++) {
		cin >> subject >> name >> time;

		tea.push_back(subject);
		tea.push_back(name);

		time_table.insert(pair<int, vector<string>>(time, tea));

		tea.clear();
	}

	cout << endl;
 
	student.assign(10, vector<vector<string>>(5, vector<string>(4, ""))); // 10����, 5��, 4��
	teacher.assign(10, vector<vector<string>>(5, vector<string>(4, ""))); // 10����, 5��, 4��

	while (grade != 4) {
		if (!createTimeTable(time_table, full_time, overlap, period, day, student, teacher, arr, grade)) {
			grade++;
		}
	}

	for (int k = 0; k < grade; k++) {
		for (int i = 0; i < student.size(); i++) {
			for (int j = 0; j < student[i].size(); j++) {
				cout << student[i][j][k] << " ";
			}
			cout << endl;
		}
	}
	/*
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

	return 0;
}

/*
âü ������ 1
âü ��äȫ 2
âü ������ 1
��� ������ 2
��� ������ 1
Ȯ�� ������ 1
Ȯ�� ������ 1
Ȯ�� ������ 1
ü�� ������ 1
ü�� ������ 1
���� ����ö 1
���� ����ö 1
���� ����ö 1
�ڹ� �ſ�� 3
�ڹ� �ſ�� 2
�ڹ� �ſ�� 1
��� ������ 1
��� ������ 1
��Ʈ �̰��� 2
��Ʈ �̰��� 1
���� ��äȫ 1
���� ��äȫ 1
���� ��äȫ 1
�̼� ������ 1
�̼� ������ 1
���� �庸�� 1
���� �庸�� 1
���� �庸�� 1

FULL_TIME 28

7 7 7 7 6

4��
*/

/*
��� ���� 1
��� ���� 2
���� �ֿ� 1
���� �ֿ� 1
���� �ֿ� 1
���� ��� 2
�ɼ� ���� 2
��� ���� 1
��� ���� 1
��� ���� 1
��ȭ ���� 2
��ȭ ��ȭ 1
���� ���� 1
���� ���� 1
���� ���� 1
���� ��� 2
���� ��� 1
�ɼ� ���� 2
�ɼ� ���� 1
�� ��� 2
�� �Ҽ� 1
���� ���� 1
�ɼ� ��� 2
ü�� �쿬 1

FULL_TIME 24

7 7 7 7 4

5��
*/