//
//  CustomProgressViewStyle.swift
//  iosApp
//
//  Created by Максим Лейхнер on 04.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI


public struct RingProgressViewStyle: ProgressViewStyle {
    private let defaultSize: CGFloat = 36
    private let lineWidth: CGFloat = 3
    private let defaultProgress = 0.2
    
    @State private var fillRotationAngle = Angle.degrees(-90)
    
    public func makeBody(configuration: ProgressViewStyleConfiguration) -> some View {
        VStack {
            configuration.label
            fillView(fractionCompleted: configuration.fractionCompleted ?? defaultProgress, isIndefinite: configuration.fractionCompleted == nil)
            configuration.currentValueLabel
        }
    }
    
    private func fillView(fractionCompleted: Double,
                          isIndefinite: Bool) -> some View {
        Circle()
            .trim(from: 0, to: CGFloat(fractionCompleted))
            .stroke(Color.white, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
            .frame(width: defaultSize - lineWidth, height: defaultSize - lineWidth)
            .rotationEffect(fillRotationAngle)
            .onAppear {
                withAnimation(.easeInOut(duration: 2).repeatForever()) {
                    fillRotationAngle = .degrees(270)
                }
            }
            .animation(.linear, value: fractionCompleted)
    }
}
