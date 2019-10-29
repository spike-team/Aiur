import com.google.common.collect.*;
import java.util.*;
import java.io.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class main {
    public static void main(String[] args) throws AssertionError {
        Scanner sc = new Scanner(System.in);
        int cnt = 0, knt = 0;

        Multimap<Integer, Table> time_table = ArrayListMultimap.create();

        int[] dy = {0, 0, 0, 0, 0};
        int[] arr = {7, 7, 7, 7, 6};

        Integer full_time = 28, day = 0, period = 0;
        ExcelParser excelParser = new ExcelParser();

        excelParser.Import(time_table);

        student_table set_table = new student_table();

        Integer grade = 0, group = 2;

        String[][][] student = new String[10][5][group];
        String[][][] teacher = new String[10][5][group];

        while (grade < group) {
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

        excelParser.Export(student, group);
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

        one:
        for (Integer i = 0; i < full_time * 50; i++) {
            if (time_table_copy.size() == 0) {
                break;
            }

            if (stu[period][day] != null)
                period++;

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

    public void Import(Multimap<Integer, Table> time_table) {
        File dirFile = new File(projectPath + "\\src\\main\\resources\\");
        File[] fileList = dirFile.listFiles();

        try {
            for (File tempFile : fileList) {
                if (tempFile.isFile()) {
                    String fileName = tempFile.getName();

                    if (!((fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) && fileName.startsWith("Temp")))
                        continue;

                    String filePath = tempFile.getAbsolutePath();
                    String[] dirName = filePath.split("\\\\");

                    FileInputStream inputStream = new FileInputStream(filePath);
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    int rows = sheet.getPhysicalNumberOfRows();

                    for (int j = 0; j < rows; j++) {
                        Vector<String> key = new Vector<>();
                        int value = 0;

                        XSSFCell cell_one = sheet.getRow(j).getCell(0);
                        XSSFCell cell_two = sheet.getRow(j).getCell(1);
                        XSSFCell cell_three = sheet.getRow(j).getCell(2);

                        key.add(cell_one.getStringCellValue());
                        key.add(cell_two.getStringCellValue());
                        value = (int) cell_three.getNumericCellValue();

                        time_table.put(j, new Table(key, value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Export(String [][][] dataArray, int group) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(projectPath + "\\test\\result.xlsx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("result");

        XSSFRow row = null;
        XSSFCell cell = null;

        for (int i = 0; i < group; i++) {
            for (int j = 0; j < dataArray.length; j++) {
                row = sheet.createRow(j + (i*10));

                for (int k = 0; k < dataArray[j].length; k++) {
                    cell = row.createCell(k);
                    cell.setCellValue(dataArray[j][k][i]);
                }
            }
        }

        try {
            wb.write(fos);

            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("정상 작동 완료.");
    }
}