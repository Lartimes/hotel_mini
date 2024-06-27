package com.lartimes.hotel.common;

import com.lartimes.hotel.model.po.Room;
import com.lartimes.hotel.model.po.Stock;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 17:51
 */
public enum ImportXlsStrategy implements Exec<Object> {
    ROOM{
        @Override
        public Object exec(Row row , int totalCells) {
            Room room = new Room();
            for (int c = 0; c < totalCells - 1; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {           //第一列
                        room.setId((int) cell.getNumericCellValue());//将单元格数据赋值给user
                    } else if (c == 1) {
                        room.setRoomType(cell.getStringCellValue());
                    } else if (c == 2) {
                        room.setRoomStatus(cell.getStringCellValue());
                    } else if (c == 3) {
                        room.setRoomPrice(cell.getNumericCellValue());
                    }else if(c == 4){
                        room.setRoomFloor(cell.getStringCellValue());
                    }else if(c == 5){
                        room.setRoomPosition(cell.getStringCellValue());
                    }else if(c == 6){
                        room.setRoomArea((int) cell.getNumericCellValue());
                    }else if(c == 7){
                        room.setBedNums((int) cell.getNumericCellValue());
                    }else if (c==8){
                        room.setPeopleNums((int) cell.getNumericCellValue());
                    } else {
                        room.setRemark(cell.getStringCellValue());
                    }
                }
            }
            return room;
        }
    },
    STOCK{
        @Override
        public Object exec(Row row , int totalCells) {
            Stock stock =  new Stock();
            for (int c = 0; c < totalCells - 1; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {           //第一列
                        stock.setId((int) cell.getNumericCellValue());//将单元格数据赋值给user
                    } else if (c == 1) {
                        stock.setName(cell.getStringCellValue());
                    } else if (c == 2) {
                        stock.setPrice(cell.getNumericCellValue());
                    } else if (c == 3) {
                        stock.setTotalNum((int) cell.getNumericCellValue());
                    }else if(c == 4){
                        stock.setOrginPrice( cell.getNumericCellValue());
                    }else {
                        stock.setUsingNum((int) cell.getNumericCellValue());
                    }
                }
            }
            return stock;
        }
    };


}
interface  Exec<T>{
    T exec(Row cell , int totalCells);
}


