import java.text.SimpleDateFormat;
import java.util.*;
import com.google.common.collect.*;
import java.io.*;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.apache.poi.ss.usermodel.CellType.FORMULA;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Integer, Table> time_table = ArrayListMultimap.create();

        int[] dy = {0, 0, 0, 0, 0};
        int[] arr = {7, 7, 7, 7, 6};

        String subject, name;
        Integer time, full_time = 28, day = 0, period = 0;

        Vector<Vector<String>> tea = new Vector<Vector<String>>(28);

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

        Integer grade = 0, ban = 2;

        String[][][] student = new String[10][5][ban];
        String[][][] teacher = new String[10][5][ban];

        while (grade < ban) {
            if
            (!set_table.createTimeTable(time_table, full_time, period, day, student, teacher, arr, grade)) {
                grade++;
            }
        }

        for (int i = 0; i < grade; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 5; k++) {
                    System.out.print(student[j][k][i] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        System.out.println(set_table.getKnt() + " " + set_table.getCnt());

        ExcelParser excelParser = new ExcelParser();
        excelParser.parsing();
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

    public boolean createTimeTable(Multimap<Integer, Table> time_table, Integer full_time, Integer periot, Integer dat, String[][][] student, String[][][] teacher, int[] arr, Integer grade) {
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

        one :
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
                                break one;
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

            for (int k = 0; k <= period; k++) {
                if (table.cource.firstElement().equals(stu[k][day])) {
                    overlap = true;
                    break;
                }
            }

            for (int l = 0; l < grade; l++) {
                if (table.cource.firstElement().equals(teacher[period][day][l])) {
                    overlap = true;
                    break;
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
                day++;
                period = 0;
            }
        }

        if (time_table_copy.isEmpty()) {
            System.out.println("size 0 out!");
            for (int i = 0; i < stu.length; i++) {
                for (int j = 0; j < stu[i].length; j++) {
                    student[i][j][grade] = stu[i][j];
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

class ExcelParser {
    public static final String projectPath = System.getProperty("user.dir");

    public void parsing() {
        Vector<Table> vector = new Vector<>();
        String zone = null;//시트명
        Table newData;

        try {
            File dirFile = new File(projectPath+"\\resources\\");
            File[] fileList = dirFile.listFiles();

            for(File tempFile : fileList) {
                if(tempFile.isFile()) {
                    String fileName = tempFile.getName();
                    if(!((fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) && fileName.startsWith("Temp"))) {
                        continue;
                    }
                    String filePath = tempFile.getAbsolutePath();
                    String[] dirName = filePath.split("\\\\");
                    zone = dirName[dirName.length-2];

                    FileInputStream inputStream = new FileInputStream(filePath);
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    int rows = sheet.getPhysicalNumberOfRows();

                    Vector<String> key = new Vector<>();

                    for(int j = 1; j <= 3; j++) {
                        for(int i = 3; i < rows; i++) {
                            XSSFCell cell = sheet.getRow(i).getCell(j);
                            String ts = "";
                            int value = 0;
                            if (cell.getCellType() == CellType.BLANK) {
                                ts = "";
                            }
                            else {
                                //타입별로 내용 읽기
                                switch (cell.getCellType()){
                                    case FORMULA:
                                        ts = cell.getCellFormula();
                                        break;
                                    case NUMERIC:
                                        ts = cell.getNumericCellValue() + " ";
                                        break;
                                    case STRING:
                                        ts = cell.getStringCellValue() + " ";
                                        break;
                                    case BLANK:
                                        ts = cell.getBooleanCellValue() + " ";
                                        break;
                                    case ERROR:
                                        ts = cell.getErrorCellValue() + " ";
                                        break;
                                }
                            }

                            String str = "";
                            int cnt = 0;

                            for (int k = 0; k < ts.length(); k++) {
                                if (ts.charAt(k) == ' ') {
                                    if (cnt == 0) {
                                        key.addElement(str);
                                        str = "";
                                    }
                                    else if (cnt == 1) {
                                        key.addElement(str);
                                        str = "";
                                    }
                                    else {
                                        value = Integer.parseInt(str);
                                        str = "";
                                    }

                                    cnt++;
                                }
                                else {
                                    str += ts.charAt(k);
                                }
                            }

                            newData = new Table(key,value);
                            vector.add(newData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Iterator<Table> iter = vector.iterator();
            XSSFWorkbook writebook = new XSSFWorkbook();//새 엑셀파일만들기
            XSSFSheet mySheet = writebook.createSheet(zone);//새 시트 만들기 (zone이라는 이름의 시트)
            int rowIndex = 0;

            XSSFRow row;
            Table d;

            while(iter.hasNext()) {
                d = iter.next();
                row = mySheet.createRow(++rowIndex);

                XSSFCell cell = row.createCell(0);
                cell.setCellValue(d.cource.firstElement());

                cell = row.createCell(1);
                cell.setCellValue(d.cource.lastElement());

                cell = row.createCell(2);
                cell.setCellValue(d.time);
            }

            FileOutputStream output = new FileOutputStream(projectPath+File.separator+"dest\\result.xls");
            writebook.write(output);//파일 생성
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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