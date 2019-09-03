#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <random>

using namespace std;

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

bool createTimeTable(multimap<int, vector<string>> time_table, int full_time, bool overlap, int period, int day, vector<vector<string>> stu, vector<int> arr) {
	multimap<int, vector<string>>::iterator iter;

	stu.clear();
	stu.assign(10, vector<string>(5, ""));
	bool success = false;

	for (int i = 0; i < full_time * 50; i++) {
		overlap = false;

		if (time_table.size() == 0) {
			break;
		}

		int number = getRandomNumber(0, time_table.size() - 1);
		iter = time_table.begin();

		while (number-- && iter != time_table.end())
			iter++;

		//cout << iter->second.front() << " " << iter->second.back() << " " << iter->first << endl;
		for (int i = 0; i <= period; i++) {
			if (iter->second.front() == stu[i][day]) {
				overlap = true;
			}
		}

		if (!overlap) {
			if (!(iter->first > 1 && period + iter->first >= 4 && period <= 3)) {
				//if (stu[iter->first + period][day] == "----") {
				if (period + iter->first > arr[day]) {
					continue;
				}
				else {
					for (int i = 0; i < iter->first; i++) {
						stu[period][day] = iter->second.front(); //+ " " + iter->second.back();
						period++;
					}

					//cout << period << endl;
					time_table.erase(iter);
				}
				//}
			}
		}

		if (period == arr[day]) {
			day++;
			period = 0;
		}
	}

	if (time_table.size() == 0) {
		for (int i = 0; i < stu.size(); i++) {
			for (int j = 0; j < stu[i].size(); j++) {
				cout << stu[i][j] << " ";
			}
			cout << endl;
		}
		cout << endl;

		return false;
	}

	return true;
}

int main() {
	ios::sync_with_stdio(false);
	cin.tie(NULL);
	cout.tie(NULL);
	
	multimap<int, vector<string>> time_table;
	vector<string> tea;
	vector<int> arr = { 7, 7, 7, 7, 6 };
	vector<vector<string>> stu, teach;
	string subject = "", name = "";
	int time = 0, full_time = 28, day = 0, period = 0;
	bool overlap = false;;

	stu.assign(10, vector<string>(5, ""));
	grade.assign(10, vector<string>(5, ""));

	for (int i = 0; i < full_time; i++) {
		cin >> subject >> name >> time;

		tea.push_back(subject);
		tea.push_back(name);

		time_table.insert(pair<int, vector<string>>(time, tea));

		tea.clear();
	}

	cout << endl;

	int n = 10;

	//for (int i = 0; i < n; i++)
	while (n > 5) {
		if (!createTimeTable(time_table, full_time, overlap, period, day, stu, arr))
			n--;
	}

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
*/