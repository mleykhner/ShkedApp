//
//  TimelineView.swift
//  iosApp
//
//  Created by Максим Лейхнер on 02.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Foundation
import Combine

struct HorizontalInfiniteScroll<Content>: View where Content: View {
    
    @State var itemSize: CGSize
    @State var spacing: CGFloat
    @ObservedObject private var scrollAnimator: HorizontalInfiniteScrollAnimator
    @ViewBuilder let viewBuilder: (Int) -> Content
    
    @State private var itemsVisible = 0

    init(
        itemSize: CGSize = CGSize(width: 46, height: 46),
        spacing: CGFloat = 12.0,
        scrollAnimator: HorizontalInfiniteScrollAnimator = HorizontalInfiniteScrollAnimator(),
        @ViewBuilder viewBuilder: @escaping (Int) -> Content
    ) {
        self.itemSize = itemSize
        self.spacing = spacing
        self.viewBuilder = viewBuilder
        self.scrollAnimator = scrollAnimator
        self.scrollAnimator.setItemWidth(itemSize.width + spacing)
    }

    var body: some View {
        
        let fullOffset = scrollAnimator.dragOffset + scrollAnimator.offset
        let fullItemSize = itemSize.width + spacing
        
        GeometryReader { proxy in
            HStack(spacing: spacing) {
                ForEach(0..<itemsVisible, id: \.self) { num in
                    let id = num - Int((fullOffset / fullItemSize).rounded()) - 1
                    viewBuilder(id)
                        .frame(width: itemSize.width, height: itemSize.height)
                }
            }
            .offset(x: -itemSize.width)
            .offset(x: fullOffset.remainder(dividingBy: fullItemSize))
            .onAppear {
                updateDatesVisible(proxy)
            }
            .gesture(
                DragGesture()
                    .onChanged { value in
                        scrollAnimator.onDrag(value)
                    }
                    .onEnded { value in
                        scrollAnimator.onDragEnded(value, duration: 2.0)
                    }
                    .simultaneously(with:
                                        TapGesture()
                        .onEnded { _ in
                            scrollAnimator.stop()
                        }

                                   )
            )
//            .gesture(
//                TapGesture()
//                    .onEnded { _ in
//                        scrollAnimator.stop()
//                    }
//            )
        }
        .frame(height: itemSize.height)
    }
    
    private func updateDatesVisible(_ proxy: GeometryProxy) {
        let frame = proxy.frame(in: .local)
        let width = frame.width
        itemsVisible = Int((width / (itemSize.width + spacing)).rounded(.up)) + 1
    }
}

#Preview {
    HorizontalInfiniteScroll() { id in
        Text("\(id)")
            .background(Color.gray)
    }
}
