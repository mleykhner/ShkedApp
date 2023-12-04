//
//  Timeline.swift
//  iosApp
//
//  Created by Максим Лейхнер on 13.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MultiPlatformLibrary
import MultiPlatformLibrarySwift
import mokoMvvmFlowSwiftUI

struct TimelineControls: View {
    let calendar: Calendar
    let monthSymbols: [String]
    init(viewModel: ScheduleScreenViewModel = ScheduleScreenViewModel()) {
        self.calendar = Calendar.current
        self.monthSymbols = calendar.standaloneMonthSymbols
        
        self._animator = StateObject(wrappedValue: TimelineAnimator())
        self._viewModel = StateObject(wrappedValue: viewModel)
        self._selectedDate = State(initialValue: viewModel.initialDate.toSwiftDate())
        self._visibleMonth = State(initialValue: Int(viewModel.initialDate.monthNumber) - 1)
        self._visibleMonthLabel = State(initialValue: monthSymbols[Int(viewModel.initialDate.monthNumber) - 1])
    }
    
    @StateObject var animator: TimelineAnimator
    @StateObject var viewModel: ScheduleScreenViewModel
    @State var selectedDate: Date {
        didSet {
            visibleMonth = calendar.component(.month, from: selectedDate) - 1
        }
    }
    
    @State var visibleMonth: Int {
        didSet {
            visibleMonthLabel = monthSymbols[visibleMonth]
        }
    }
    @State var visibleMonthLabel: String
    
    var body: some View {
        VStack(spacing: 12) {
            HStack {
                Menu {
                    ForEach(monthSymbols, id: \.self) { month in
                        Button(month.capitalized) {
                            
                        }
                    }
                } label: {
                    Text(visibleMonthLabel.capitalized)
                        .underline(color: Color.blue)
                        .foregroundStyle(Color.black)
                        .font(unboundedFontFamily.bold(size: 20))
                }
                Spacer()
                Button {
                    viewModel.backToToday()
                } label: {
                    Image(systemName: "ellipsis")
                        .font(.system(size: 24))
                }
            }
            .padding([.leading, .trailing], 18)
            Timeline(
                viewModel: viewModel,
                animator: animator,
                visibleMonth: $visibleMonth
            )
        }
        .padding([.top, .bottom], 18)
        .background(Material.thick)
        .clipShape(RoundedCorners(radius: 28, corners: [.topLeft, .topRight]))
        .onReceive(createPublisher(viewModel.actions), perform: { action in
            let actionKs = ScheduleScreenViewModelActionKs(action)
            if actionKs != .dateChanged { return }
            selectedDate = viewModel.selectedDate.toSwiftDate()
            
            
        })
    }
}

extension Kotlinx_datetimeLocalDate {
    func toSwiftDate() -> Date {
        return Date(timeIntervalSince1970: Double(self.toEpochDays()) * 86_400)
    }
}

extension Date {
    func toLocalDate() -> Kotlinx_datetimeLocalDate {
        let calendar = Calendar.current
        return Kotlinx_datetimeLocalDate(
            year: Int32(calendar.component(.year, from: self)),
            monthNumber: Int32(calendar.component(.month, from: self)),
            dayOfMonth: Int32(calendar.component(.day, from: self))
        )
    }
}

#Preview {
    TimelineControls()
}
