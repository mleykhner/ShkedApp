////
////  CalendarView.swift
////  shked
////
////  Created by Максим Лейхнер on 19.02.2023.
////
//
//import SwiftUI
//import SwiftUIPager
//
//struct CalendarView: View {
//    
//    //let onDateChange: (Date) -> Any
//    
//    @Binding var selectedDay: Date
//    @State var draggingProgress: CGFloat = 48
//    @State var visibleObserver: Bool = true
//    
//    var body: some View {
//        VStack(spacing: 6){
//            Capsule()
//                .frame(width: 84, height: 4)
//                .background(.ultraThinMaterial)
//            VStack(spacing: 12){
//                HStack {
//                    Text(selectedDay.getMonthSymbol().capitalized)
//                        .font(.custom("Unbounded", size: 20))
//                        .fontWeight(.bold)
//                        .frame(maxWidth: .infinity, alignment: .leading)
//                    Spacer()
//                    Menu {
//                        if !$selectedDay.wrappedValue.isSameAs(Date()){
//                            Button {
//                                selectedDay = Date()
//                            } label: {
//                                HStack{
//                                    Text("backToToday")
//                                    Image(systemName: "calendar.circle")
//                                }
//                            }
//                        }
//                        Button {
//                            //Изменить группу
//                            
//                        } label: {
//                            HStack {
//                                Text("changeGroup")
//                                Image(systemName: "person.2.badge.gearshape")
//                            }
//                        }
//                        Button(role: .destructive) {
//                            //Сообщить об ошибке
//                        } label: {
//                            HStack {
//                                Text("reportABug")
//                                Image(systemName: "exclamationmark.triangle")
//                            }
//                        }
//                        
//                    } label: {
//                        Image(systemName: "ellipsis")
//                            .font(.system(size: 24))
//                            .frame(width: 26, height: 26)
//                    }
//                }
//                .foregroundColor(Color("onSurface"))
//                Group {
//                    if visibleObserver{
//                        WeekObserverView($selectedDay)
//                            .padding(.horizontal, -18)
//                    } else {
//                        MonthObserverView(selectedDay: $selectedDay, onDateChange: {
//                            _ in
//                            closeMonthView()
//                        })
//                        .frame(maxHeight: 300)
//                        .padding(.horizontal, -18)
//                        .transition(.opacity.animation(.easeOut(duration: 0.3)))
//                    }
//                }
//                .frame(maxHeight: draggingProgress)
//                
//                
//                HStack{
//                    ForEach(fetchWeekdays(), id: \.self){
//                        day in
//                        Text(day.uppercased())
//                            .font(.custom("Unbounded", size: 12))
//                            .fontWeight(.semibold)
//                            .foregroundColor(Color(tm.getTheme().foregroundColor).opacity(0.3))
//                            .frame(maxWidth: .infinity)
//                    }
//                }
//            }
//            .padding(18)
//            .frame(maxWidth: .infinity)
//            .background(
//                RoundedCorners(radius: 30, corners: [.topLeft, .topRight])
//                    .fill(Material.thin)
//            )
//        }
//        .gesture(
//            DragGesture()
//                .onChanged({ offset in
//                    draggingProgress -= offset.translation.height
//                    
//                    
//                    if draggingProgress / (300 - 48) > 0.5 {
//                        visibleObserver = false
//                    } else {
//                        visibleObserver = true
//                    }
//                    
//                    if draggingProgress < 48{
//                        draggingProgress = 48
//                    } else if draggingProgress > 300 {
//                        draggingProgress = 300
//                    }
//                })
//                .onEnded({ offset in
//                    if draggingProgress / (300 - 48) > 0.5 {
//                        withAnimation {
//                            draggingProgress = 300
//                        }
//                    } else {
//                        withAnimation {
//                            draggingProgress = 48
//                        }
//                    }
//                })
//        )
//    }
//    
//    func closeMonthView(){
//        withAnimation {
//            visibleObserver = true
//            draggingProgress = 48
//        }
//        //vm.objectWillChange.send()
//    }
//}
//
//struct CalendarView_Previews: PreviewProvider {
//    @State static var selectedDay = Date()
//    static var previews: some View {
//        ZStack{
//            Color.black.ignoresSafeArea()
//            CalendarView(selectedDay: $selectedDay)
//        }
//    }
//}
//
//struct RoundedCorners: Shape {
//    var radius: CGFloat
//    var corners: UIRectCorner
//    
//    func path(in rect: CGRect) -> Path
//    {
//        let path = UIBezierPath(
//            roundedRect: rect,
//            byRoundingCorners: corners,
//            cornerRadii: CGSize(
//                width: radius,
//                height: radius))
//        
//        return Path(path.cgPath)
//    }
//}
//
//extension Date {
//    func getMonthSymbol() -> String{
//        let calendar = Calendar.current
//        return calendar.standaloneMonthSymbols[calendar.component(.month, from: self) - 1]
//    }
//    
//    func isSameAs (_ date: Date) -> Bool{
//        let calendar = Calendar.current
//        return calendar.isDate(self, inSameDayAs: date)
//    }
//}
//
//
//
//
//func fetchWeekdays() -> [String] {
//    //Подключаем календарь пользователя
//    let calendar = Calendar.current
//    //Забираем локализованные названия дней недели
//    let weekdays = calendar.veryShortWeekdaySymbols
//    //Забираем номер первого дня недели
//    let firstDay = calendar.firstWeekday
//    //Переносим дни согласно этому номеру
//    var result: [String] = []
//    for i in (0..<7){
//        //Добавляем в массив
//        result.append(weekdays[(i + firstDay - 1) % 7])
//    }
//    return result
//}
