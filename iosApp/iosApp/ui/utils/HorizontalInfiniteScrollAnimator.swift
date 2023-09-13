//
//  HorizontalScrollAnimator.swift
//  iosApp
//
//  Created by Максим Лейхнер on 10.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

class HorizontalInfiniteScrollAnimator: ObservableObject {
    
    @Published private var animationTimer: Timer? = nil

    @Published private(set) var dragOffset: CGFloat = 0.0
    @Published private(set) var offset: CGFloat = 0.0
    
    var isAnimationFinished: Bool {
        !(animationTimer?.isValid ?? false)
    }
    
    private var startPosition: CGFloat = 0
    private var endPosition: CGFloat = 0
    private var scrollDuration: Double = 0
    private var startTime: TimeInterval = 0
    
    func animate(from start: CGFloat, to end: CGFloat, duration: Double = 1.0) {
        startPosition = start
        endPosition = end
        scrollDuration = duration
        startTime = CACurrentMediaTime()
        animationTimer = Timer.scheduledTimer(withTimeInterval: 1/60, repeats: true) { _ in
            self.offset = self.nextStep()
        }
    }
    
    func scrollTo(_ end: CGFloat, duration: Double = 1.0) {
        animate(from: offset, to: end, duration: duration)
    }
    
    func scrollTo(_ id: Int, duration: Double = 1.0) {
        //TODO: Сделать скролл к определенному элементу
    }
    
    func scroll(_ delta: CGFloat, duration: Double = 1.0) {
        animate(from: offset, to: offset + delta, duration: duration)
    }
    
    func stop() {
        animationTimer?.invalidate()
        startPosition = 0
        endPosition = 0
        scrollDuration = 0
        startTime = 0
    }
    
    func onDrag(_ value: DragGesture.Value) {
        dragOffset = value.translation.width
    }
    
    func onDragEnded(_ value: DragGesture.Value, minDelta: CGFloat = 50, duration: Double = 1.0) {
        let predictedWidth = value.predictedEndTranslation.width
        if abs(value.translation.width - value.predictedEndTranslation.width) >= minDelta {
            animate(from: offset, to: (offset + predictedWidth), duration: duration)
        }
        offset += dragOffset
        dragOffset = 0
    }
    
    func nextStep() -> CGFloat {
        let currentTime = CACurrentMediaTime()
        
        let time = TimeInterval(min(1.0, (currentTime - startTime) / scrollDuration))
        
        if time >= 1.0 {
            animationTimer?.invalidate()
            return endPosition
        }
        
        let delta = easeOut(time: time)
        let scrollOffset = startPosition + (endPosition - startPosition) * CGFloat(delta)
        
        return scrollOffset
    }
    
    private func easeOut(time: TimeInterval) -> TimeInterval {
        return 1 - pow((1 - time), 4)
    }
}
