//
//  TimelineView.swift
//  iosApp
//
//  Created by Максим Лейхнер on 02.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Foundation
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary
import MultiPlatformLibrarySwift

struct Timeline: View {
    private let calendar: Calendar = Calendar.current
    private let itemSize: CGSize = CGSize(width: 46, height: 67)
    private let spacing: CGFloat = 12.0
    private var weekdaySymbols: [String] = []
    @State private var selectedDate: Date = Date()
    @StateObject private var viewModel: ScheduleScreenViewModel
    @ObservedObject private var animator: TimelineAnimator
    @State private var itemsVisible = 0
    @Binding private var visibleMonth: Int
    
    init(
        viewModel: ScheduleScreenViewModel,
        animator: TimelineAnimator = TimelineAnimator(),
        visibleMonth: Binding<Int>
    ) {
        self._viewModel = StateObject(wrappedValue: viewModel)
        self._selectedDate = State(initialValue: viewModel.initialDate.toSwiftDate())
        self._visibleMonth = visibleMonth
        self.animator = animator
        self.animator.setItemWidth(itemSize.width + spacing)
        self.weekdaySymbols = calendar.shortStandaloneWeekdaySymbols
    }
    
    var body: some View {
//        let fullItemWidth = itemSize.width + spacing
//        let dateOffset = Int((animator.fullOffset / fullItemWidth).rounded())
        
        GeometryReader { proxy in
            HStack(spacing: spacing) {
                ForEach(0 ..< itemsVisible, id: \.self) { num in
                    let id = num - animator.dateOffset - 1
                    let date = calendar.date(byAdding: .day, value: id, to: Date()) ?? Date()
                    let weekday = weekdaySymbols[
                        calendar.component(.weekday, from: date) - 1
                    ]
                    VStack(alignment: .center) {
                        Text(date.extractDate("dd"))
                            .font(unboundedFontFamily.bold(size: 20))
                            .foregroundStyle(
                                calendar.component(.month, from: date) - 1 == visibleMonth ? Color.black : Color.gray
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
//                    .onTapGesture {
//                        viewModel.selectedDate = date.toLocalDate()
//                    }
                    .gesture(
                        TapGesture()
                            .onEnded { _ in
                                viewModel.selectedDate = date.toLocalDate()
                            }
                    )
                        .frame(width: itemSize.width, height: itemSize.height)
                }
            }
            .offset(x: -itemSize.width)
            .offset(x: animator.fullOffset.remainder(dividingBy: animator.itemWidth ?? 1))
            .onAppear {
                updateDatesVisible(proxy)
            }
//            .highPriorityGesture(
//                DragGesture(minimumDistance: 0)
//                .onChanged { value in
//                    scrollAnimator.onDrag(value)
//                }
//                .onEnded { value in
//                    scrollAnimator.onDragEnded(value, duration: 2.0)
//                }
//            )
            .gesture(
                DragGesture(minimumDistance: 0)
                    .onChanged { value in
                        animator.onDrag(value)
                    }
                    .onEnded { value in
                        animator.onDragEnded(value, duration: 2.0)
                    }
//                    .simultaneously(with: TapGesture())
            )
        }
        .frame(height: itemSize.height)
        .onReceive(createPublisher(viewModel.actions), perform: { action in
            let actionKs = ScheduleScreenViewModelActionKs(action)
            if actionKs != .dateChanged { return }
            let secondVisibleDay = calendar.date(byAdding: .day, value: 1 - animator.dateOffset, to: viewModel.initialDate.toSwiftDate()) ?? viewModel.initialDate.toSwiftDate()
            
            let secondToLastVisibleDay = (calendar.date(byAdding: .day, value: itemsVisible - 4, to: secondVisibleDay) ?? secondVisibleDay)
            
            selectedDate = viewModel.selectedDate.toSwiftDate()
            
            if selectedDate < secondVisibleDay {
                let delta = selectedDate.distance(to: secondVisibleDay) / 86_400
                let offset = animator.itemWidth?.scaled(by: delta) ?? 0
                animator.scroll(offset, duration: 1)
            } else if selectedDate > secondToLastVisibleDay {
                let delta = selectedDate.distance(to: secondToLastVisibleDay) / 86_400
                let offset = animator.itemWidth?.scaled(by: delta) ?? 0
                animator.scroll(offset, duration: 1)
            }
            
        })
//        .onChange(of: animator.dateOffset, perform: { _ in
//            let initialDate = viewModel.initialDate.toSwiftDate()
//            let visibleMonths = (0 ..< itemsVisible).map({
//                let date = calendar.date(byAdding: .day, value: $0 - animator.dateOffset, to: initialDate) ?? initialDate
//                return calendar.component(.month, from: date) - 1
//            })
//            if !visibleMonths.contains(visibleMonth) {
//                visibleMonth = visibleMonths.last ?? visibleMonth
//            }
////            for i in 0 ..< itemsVisible {
////                let id = i - animator.dateOffset
////                let initialDate = viewModel.initialDate.toSwiftDate()
////                let date = calendar.date(byAdding: .day, value: id, to: initialDate) ?? initialDate
////                if calendar.component(.month, from: date) - 1 == visibleMonth {
////                    
////                }
////            }
//        })
    }
    
    private func updateDatesVisible(_ proxy: GeometryProxy) {
        let frame = proxy.frame(in: .local)
        let width = frame.width
        itemsVisible = Int((width / (itemSize.width + spacing)).rounded(.up)) + 1
    }
}

#Preview {
    @State var month = 1
    return Timeline(viewModel: ScheduleScreenViewModel(), visibleMonth: $month)
}
