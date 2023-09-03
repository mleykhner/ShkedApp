////
////  WeekObserver.swift
////  iosApp
////
////  Created by Максим Лейхнер on 18.08.2023.
////  Copyright © 2023 orgName. All rights reserved.
////
//
//import SwiftUI
//import SwiftUIPager
//
//struct WeekObserverView: View {
//    
//    init(_ day: Binding<Date>){
//        _selectedDay = day
//        _originDate = State(initialValue: day.wrappedValue)
//    }
//    
//    @Binding var selectedDay: Date
//    @State var originDate: Date
//    
//    @State var page: Page = Page.withIndex(3)
//    @State var previousPageIndex:Int = 3
//    @State var deltaWeeks: [Int] = [-3, -2, -1, 0, 1, 2, 3]
//    @State var disableButtons: Bool = false
//    
//    let generator = UISelectionFeedbackGenerator()
//    
//    var body: some View {
//        Pager(page: page, data: deltaWeeks, id: \.self) { deltaWeek in
//            HStack{
//                ForEach(fetchWeek(originDate.deltaWeek(delta: deltaWeek)), id: \.self ){ day in
//                    Button {
//                        generator.selectionChanged()
//                        selectedDay = day
//                        
//                    } label: {
//                        Text(day.extractDate("dd"))
//                            .font(unboundedFontFamily.bold(size: 20))
//                            .frame(width: 42, height: 42)
//                            .overlay(content: {
//                                RoundedRectangle(cornerRadius: 15, style: .continuous)
//                                    .stroke(lineWidth: selectedDay.isSameAs(day) ? 2 : 0)
//                                    .frame(width: 46, height: 46)
//                                    .animation(.easeOut, value: selectedDay)
//                            })
//                            .foregroundColor(Color("onSurface").opacity(day.isSameMonth(selectedDay) ? 1 : 0.4))
//                            .frame(maxWidth: .infinity)
//                        
//                    }
//                    .disabled(disableButtons)
//                }
//            }.padding(.horizontal, 18)
//        }
//        .singlePagination()
//        .pagingPriority(.simultaneous)
//        .onDraggingBegan({
//            disableButtons = true
//            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
//                disableButtons = false
//            })
//        })
//        .onPageChanged({ _ in
//            print("Page changed")
//            if page.index >= deltaWeeks.count - 2 {
//                (1..<4).forEach { _ in
//                    deltaWeeks.append((deltaWeeks.last ?? 0) + 1)
//                    deltaWeeks.removeFirst()
//                    page.index -= 1
//                }
//                selectedDay = selectedDay.deltaWeek(delta: 1)
//            } else if page.index <= 1 {
//                (1..<4).forEach { _ in
//                    deltaWeeks.insert((deltaWeeks.first ?? 0) - 1, at: 0)
//                    deltaWeeks.removeLast()
//                    page.index += 1
//                }
//                selectedDay = selectedDay.deltaWeek(delta: -1)
//            } else {
//                selectedDay = selectedDay.deltaWeek(delta: page.index - previousPageIndex)
//            }
//            previousPageIndex = page.index
//            generator.selectionChanged()
//            
//        })
//        .itemSpacing(50)
//        .frame(height: 48)
//        .onChange(of: selectedDay) { newDay in
//            var sameWeek = false
//            let week = fetchWeek(originDate.deltaWeek(delta: deltaWeeks[page.index]))
//            week.forEach { day in
//                if day.isSameAs(newDay) {
//                    sameWeek = true
//                }
//            }
//            if !sameWeek {
//                print("Different week")
//                if newDay.isSameAs(week.last?.getNextDay() ?? Date()) {
//                    page.update(.next)
//                } else if newDay.isSameAs(week.first?.getPreviousDay() ?? Date()) {
//                    page.update(.previous)
//                } else {
//                    deltaWeeks = [-3, -2, -1, 0, 1, 2, 3]
//                    page.index = 3
//                    previousPageIndex = 3
//                    originDate = newDay
//                }
//                previousPageIndex = page.index
//            }
//        }
//        
//    }
//}
//
//struct WeekObserverView_Previews: PreviewProvider {
//    @State static var selectedDay: Date = Date()
//    static var originDate: Date = Date()
//    static var previews: some View {
//        WeekObserverView($selectedDay)
//    }
//}
//
//extension View {
//    
//    @ViewBuilder
//    public func `if`<T: View, U: View>(
//        _ condition: Bool,
//        then modifierT: (Self) -> T,
//        else modifierU: (Self) -> U
//    ) -> some View {
//        
//        if condition { modifierT(self) }
//        else { modifierU(self) }
//    }
//}
//
//extension Date {
//    
//    func getNextDay() -> Date {
//        return self.addingTimeInterval(86400)
//    }
//    
//    func getPreviousDay() -> Date {
//        return self.addingTimeInterval(-86400)
//    }
//}
//
//fileprivate func fetchWeek(_ date: Date = Date()) -> [Date] {
//    //Подключем колендать пользователя
//    let calendar = Calendar.current
//    //Получаем текущую неделю
//    let week = calendar.dateInterval(of: .weekOfMonth, for: date)
//    //Находим первый день недели
//    guard let firstWeekDay = week?.start else{
//        return []
//    }
//    
//    var result: [Date] = []
//    
//    //Добавляем дни
//    (0...6).forEach{
//        day in
//        //Переходим к следующему дню
//        if let weekday = calendar.date(byAdding: .day, value: day, to: firstWeekDay){
//            //Добавляем в массив
//            result.append(weekday)
//        }
//    }
//    return result
//}
