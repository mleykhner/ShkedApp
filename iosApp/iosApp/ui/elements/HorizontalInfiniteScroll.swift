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

struct HorizontalInfiniteScroll<Content: View>: View {
    
    @State var itemSize: CGSize = CGSize(width: 46, height: 46)
    @State var spacing: CGFloat = 12.0
    
    @StateObject private var scrollAnimator = HorizontalScrollAnimator()
    
    @State private var itemsVisible = 0
    
    @ViewBuilder let viewBuilder: (Int) -> Content

    var body: some View {
        
        let fullOffset = scrollAnimator.dragOffset + scrollAnimator.offset
        let fullItemSize = itemSize.width + spacing
        
        GeometryReader { proxy in
            HStack(spacing: spacing) {
                ForEach(0..<itemsVisible, id: \.self) { num in
                    let id = num - Int((fullOffset / fullItemSize).rounded())
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
            )
            .gesture(
                TapGesture()
                    .onEnded { _ in
                        scrollAnimator.stop()
                    }
            )
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
            .padding()
            .background(Color.gray)
    }
}
