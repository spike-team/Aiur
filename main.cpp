#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <random>

using namespace std;

int cnt = 0, knt = 0;

int getRandomNumber(int min, int max)
{
	//< 1단계. 시드 설정
	random_device rn;
	mt19937_64 rnd(rn());

	//< 2단계. 분포 설정 ( 정수 )
	uniform_int_distribution<int> range(min, max);

	//< 3단계. 값 추출
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
	vector<int> arr = { 7, 7, 7, 7, 6 }; // 월, 화, 수, 목, 금 각각 총 교시
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
 
	student.assign(10, vector<vector<string>>(5, vector<string>(4, ""))); // 10교시, 5일, 4반
	teacher.assign(10, vector<vector<string>>(5, vector<string>(4, ""))); // 10교시, 5일, 4반

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

FULL_TIME 28

7 7 7 7 6

4반
*/

/*
고생 최윤 1
고생 손희 2
역사 최연 1
역사 최연 1
역사 최연 1
생윤 양순 2
심수 윤옥 2
논술 정선 1
논술 정선 1
논술 정선 1
고화 정재 2
고화 이화 1
영어 정영 1
영어 정영 1
영어 정영 1
고지 김숙 2
고지 노경 1
심수 유익 2
심수 유익 1
고물 고관 2
고물 소순 1
진로 김은 1
심수 김시 2
체육 우연 1

FULL_TIME 24

7 7 7 7 4

5반
*/