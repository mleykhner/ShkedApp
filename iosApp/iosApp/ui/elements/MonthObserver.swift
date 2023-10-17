////
////  MonthObserver.swift
////  iosApp
////
////  Created by Максим Лейхнер on 18.08.2023.
////  Copyright © 2023 orgName. All rights reserved.
////
//
//import Foundation
//import SwiftUI
//import SwiftUIPager
//
//struct MonthObserverView: View {
//    
//    init(selectedDay: Binding<Date>, onDateChange: @escaping (Date) -> Any = {_ in return}){
//        self._selectedDay = selectedDay
//        self._originDate = State(initialValue: selectedDay.wrappedValue)
//        self.onDateChange = onDateChange
//    }
//    
//    @Binding var selectedDay: Date
//    let onDateChange: (Date) -> Any
//    
//    @State var page: Page = Page.withIndex(1)
//    @State var previousPageIndex: Int = 1
//    @State var deltaMonths: [Int] = [-1, 0, 1]
//    @State var disableButtons: Bool = false
//    @State var originDate: Date
//    
//    private let generator = UISelectionFeedbackGenerator()
//    
//    let columns = Array(repeating: GridItem(.flexible()), count: 7)
//    
//    var body: some View {
//        Text("Hello")
////        Pager(page: page, data: deltaMonths, id: \.self) {
////            month in
////            LazyVGrid(columns: columns) {
////                ForEach (fetchMonth(originDate.deltaMonth(delta: month)), id: \.self) { day in
////                    Button {
////                        generator.selectionChanged()
////                        selectedDay = day
////                        _ = onDateChange(day)
////                    } label: {
////                        Text(day.extractDate("dd"))
////                            .font(unboundedFontFamily.bold(size: 20))
////                            .frame(width: 42, height: 42)
////                            .overlay(content: {
////                                RoundedRectangle(cornerRadius: 15, style: .continuous)
////                                    .stroke(lineWidth: selectedDay.isSameAs(day) ? 2 : 0)
////                                    .frame(width: 46, height: 46)
////                                    .animation(.easeOut, value: selectedDay)
////                            })
////                            .foregroundColor(Color("onSurface"))
////                            .frame(maxWidth: .infinity)
////                        
////                    }
////                    .disabled(disableButtons || day == Date.distantPast)
////                    .opacity(day == Date.distantPast ? 0 : 1)
////                    
////                    
////                    
////                }
////            }
////            .padding(.horizontal, 18)
////        }
////        .singlePagination()
////        .pagingPriority(.simultaneous)
////        .onDraggingBegan({
////            disableButtons = true
////            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
////                disableButtons = false
////            })
////        })
////        .onPageChanged({ _ in
////            print("Page changed")
////            if page.index >= deltaMonths.count - 1 {
////                deltaMonths.append((deltaMonths.last ?? 0) + 1)
////                deltaMonths.removeFirst()
////                page.index -= 1
////                selectedDay = selectedDay.deltaMonth(delta: 1)
////            } else if page.index <= 1 {
////                deltaMonths.insert((deltaMonths.first ?? 0) - 1, at: 0)
////                deltaMonths.removeLast()
////                page.index += 1
////                selectedDay = selectedDay.deltaMonth(delta: -1)
////            } else {
////                selectedDay = selectedDay.deltaMonth(delta: page.index - previousPageIndex)
////            }
////            previousPageIndex = page.index
////            generator.selectionChanged()
////        })
//    }
//}
//
//struct MonthObserverView_Previews: PreviewProvider {
//    
//    @State static var selectedDay: Date = Date()
//    
//    static var previews: some View {
//        MonthObserverView(selectedDay: $selectedDay)
//    }
//}
//
//fileprivate func fetchMonth(_ date: Date = Date()) -> [Date]{
//    //Подключем колендать пользователя
//    let calendar = Calendar.current
//    
//    //Получаем месяц
//    let month = calendar.dateInterval(of: .month, for: date)
//    
//    //Находим первый день месяца
//    guard let firstMonthDay = month?.start else{
//        return []
//    }
//    
//    var result = calendar.range(of: .day, in: .month, for: date)!.compactMap { (dayNum: Int) -> Date in
//        return calendar.date(byAdding: .day, value: dayNum - 1, to: firstMonthDay)!
//    }
//    
//    let firstWeekDay = calendar.component(.weekday, from: result.first!)
//    
//    for _ in 0..<(7 + firstWeekDay - calendar.firstWeekday) % 7 {
//        result.insert(Date.distantPast, at: 0)
//    }
//    
//    return result
//}
//
//extension Date {
//    func deltaMonth(delta: Int) -> Date{
//        let calendar = Calendar.current
//        return calendar.date(byAdding: .month, value: delta, to: self)!
//    }
//}
