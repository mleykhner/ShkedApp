//
//  DateExtension.swift
//  iosApp
//
//  Created by Максим Лейхнер on 13.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension Date {
    func extractDate(_ format: String) -> String{
        //Подключаем форматировщик дат
        let formatter = DateFormatter()
        //Устанавливаем формат
        formatter.dateFormat = format
        //Разбиваем отформатированную дату на части
        let parts = formatter.string(from: self).split(separator: "/")
        var newDate = ""
        //Собираем обратно удаляя незначащие нули
        for i in 0..<parts.count {
            newDate = "\(newDate)\(i == 0 ? "" : "/")\(Int(parts[i])!)"
        }
        return newDate
    }
    
    func getMonthSymbol() -> String{
        let calendar = Calendar.current
        return calendar.standaloneMonthSymbols[calendar.component(.month, from: self) - 1]
    }
    
    func isSameAs (_ date: Date) -> Bool{
        let calendar = Calendar.current
        return calendar.isDate(self, inSameDayAs: date)
    }
    
    func isSameMonth (_ date: Date) -> Bool{
        let calendar = Calendar.current
        return calendar.isDate(date, equalTo: self, toGranularity: .month)
    }
}
