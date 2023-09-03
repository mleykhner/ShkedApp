//
//  TimelineView.swift
//  iosApp
//
//  Created by Максим Лейхнер on 02.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct TimelineView: View {
    
    @GestureState private var dragOffset = CGSize.zero
    @State var viewOffset = CGFloat(-42 + 6)
    @State var dataOffset = 0
    
    var body: some View {
        GeometryReader { proxy in
            HStack(spacing: 12) {
                ForEach(0..<((Int(proxy.size.width) / (42 + 12)) + 2), id: \.self) { num in
                    Text("\(num + dataOffset)")
                        .frame(width: 42, height: 42)
                }
            }
            .offset(x: viewOffset)
            .gesture(
                DragGesture()
                    .updating($dragOffset) { value, state, transaction in
                        state = value.translation
                        viewOffset = value.translation.x % (42 + 12)
                    }
            )
        }
    }
}

#Preview {
    TimelineView()
}
