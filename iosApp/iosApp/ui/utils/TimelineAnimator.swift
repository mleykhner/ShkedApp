//
//  HorizontalScrollAnimator.swift
//  iosApp
//
//  Created by Максим Лейхнер on 10.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

class TimelineAnimator: ObservableObject {
    
    private(set) var dragOffset: CGFloat = 0.0 {
        didSet {
            fullOffset = dragOffset + offset
        }
    }
    private(set) var offset: CGFloat = 0.0 {
        didSet {
            fullOffset = dragOffset + offset
        }
    }
    @Published private(set) var fullOffset: CGFloat = 0.0 {
        didSet {
            if itemWidth != nil && itemWidth != CGFloat.zero {
                dateOffset = Int((fullOffset / itemWidth!).rounded())
            }
        }
    }
    @Published private(set) var dateOffset: Int = 0
    @Published private(set) var itemWidth: CGFloat? = nil
    private var displayLink: CADisplayLink? = nil
    
    var isAnimationFinished: Bool {
        displayLink == nil
    }
    
    private var startPosition: CGFloat = 0
    private var endPosition: CGFloat = 0
    private var scrollDuration: Double = 0
    private var startTime: TimeInterval = 0
    
    func animate(from start: CGFloat, to end: CGFloat, duration: Double = 1.0) {
        stop()
        startPosition = start
        endPosition = end
        scrollDuration = duration
        startTime = CACurrentMediaTime()
        displayLink = CADisplayLink(target: self, selector: #selector(step))
        displayLink?.add(to: .current, forMode: .default)
    }
    
    func setItemWidth(_ width: CGFloat) {
        itemWidth = width
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
        displayLink?.invalidate()
        displayLink = nil
    }
    
    func onDrag(_ value: DragGesture.Value) {
        if !isAnimationFinished { stop() }
        dragOffset = value.translation.width
    }
    
    func onDragEnded(_ value: DragGesture.Value, minDelta: CGFloat = 50, duration: Double = 1.0) {
        offset += dragOffset
        dragOffset = 0
        let predictedWidth = value.predictedEndTranslation.width
        if abs(value.translation.width - value.predictedEndTranslation.width) >= minDelta {
            animate(from: offset, to: (offset + predictedWidth), duration: duration)
        }
    }
    
    func nextStep() -> CGFloat {
        let currentTime = CACurrentMediaTime()
        
        let time = TimeInterval(min(1.0, (currentTime - startTime) / scrollDuration))
        
        if time >= 1.0 {
            stop()
            return endPosition
        }
        
        let delta = easeOut(time: time)
        let scrollOffset = startPosition + (endPosition - startPosition) * CGFloat(delta)
        
        return scrollOffset
    }
    
    private func easeOut(time: TimeInterval) -> TimeInterval {
        return 1 - pow((1 - time), 4)
    }
    
    @objc private func step() {
        self.offset = self.nextStep()
    }
}
