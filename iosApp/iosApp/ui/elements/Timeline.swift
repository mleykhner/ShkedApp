//
//  Timeline.swift
//  iosApp
//
//  Created by Максим Лейхнер on 13.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct Timeline: View {
    private let calendar = Calendar.current
    @StateObject var animator = HorizontalInfiniteScrollAnimator()
    @State var selectedDate = Date()
    
    var body: some View {
        VStack(spacing: 12) {
            HStack {
                Menu {
//                    ForEach(calendar.standaloneMonthSymbols.enumerated(), id: \.offset) {
//                        
//                    }
                    Button {
                        
                    } label: {
                        Text("Январь")
                    }
                } label: {
                    Text(selectedDate.getMonthSymbol().capitalized)
                        .underline(color: Color.blue)
                        .foregroundStyle(Color.black)
                        .font(unboundedFontFamily.bold(size: 20))
                }
                Spacer()
                Button {
                    
                } label: {
                    Image(systemName: "ellipsis")
                        .font(.system(size: 24))
                }
            }
            .padding([.leading, .trailing], 18)
            let weekdays = calendar.veryShortWeekdaySymbols
            HorizontalInfiniteScroll(
                itemSize: CGSize(width: 46, height: 67),
                scrollAnimator: animator
            ) { id in
                let date = calendar.date(byAdding: .day, value: id, to: Date()) ?? Date()
                let weekday = weekdays[
                    calendar.component(.weekday, from: date) - 1
                ]
                VStack(alignment: .center) {
                    Text(date.extractDate("dd"))
                        .font(unboundedFontFamily.bold(size: 20))
                        .foregroundStyle(
                            date.isSameMonth(selectedDate) ? Color.black : Color.gray
                        )
                        .overlay(content: {
                            RoundedRectangle(cornerRadius: 15, style: .continuous)
                                .stroke(lineWidth: selectedDate.isSameAs(date) ? 2 : 0)
                                .frame(width: 46, height: 46)
                                .animation(.easeOut, value: selectedDate)
                        })
                        .frame(width: 46, height: 46)
                    Spacer()
                    Text(weekday.uppercased())
                        .font(unboundedFontFamily.semiBold(size: 12))
                }
                .gesture(
                    TapGesture()
                        .onEnded { _ in
                            selectedDate = date
                        }
                )
            }
        }
        .padding([.top, .bottom], 18)
        .background(Material.thick)
        .clipShape(RoundedCorners(radius: 28, corners: [.topLeft, .topRight]))
    }
}

#Preview {
    Timeline()
}
