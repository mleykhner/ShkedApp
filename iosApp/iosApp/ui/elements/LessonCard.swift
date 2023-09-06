//
//  LessonCard.swift
//  iosApp
//
//  Created by Максим Лейхнер on 11.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LessonCard: View {
    
    @State var lesson: LessonViewData
    
    @State var expanded: Bool = false
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                LessonOrdinal(ordinal: Int(lesson.ordinal))
                VStack(alignment: .leading) {
                    Text(
                        Strings()
                            .get(
                                id: SharedRes.strings().now,
                                args: []
                            )
                            .uppercased())
                        .font(titleSmallStyle)
                        .foregroundStyle(Color("secondary"))
                    Spacer().frame(height: 2)
                    Text(lesson.name)
                        .font(titleMediumStyle)
                        .foregroundStyle(Color("onSurface"))
                    Spacer().frame(height: 8)
                    HStack {
                        HStack(spacing: 14) {
                            Text(lesson.type.toLocalizedString())
                                .font(golosFontFamily.bold(size: 14))
                                .padding(.horizontal, 6)
                                .padding(.vertical, 3)
                                .background {
                                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                                        .stroke(lineWidth: 2.4)
                                }
                            Text(lesson.location)
                                .font(golosFontFamily.semiBold(size: 14))
                            Text("10:45 – 12:15")
                                .font(golosFontFamily.medium(size: 14))
                        }
                        .foregroundStyle(Color("secondary"))
                        Spacer()
                        HStack {
                            Circle()
                                .frame(width: 8, height: 8)
                                .foregroundStyle(Color("primary"))
                            Button(action: {
                                    withAnimation {
                                        expanded.toggle()
                                    }
                                }
                            ) {
                                Image(
                                    systemName: expanded ? "chevron.compact.up" : "chevron.compact.down"
                                )
                                    .font(.system(size: 24))
                            }
                            .tint(Color("onSurface"))
                            .modify {
                                if #available(iOS 17, *) {
                                    $0.contentTransition(.symbolEffect(.replace))
                                } else if #available(iOS 16, *) {
                                    $0.contentTransition(.opacity)
                                }
                            }
                            
                        }
                    }
                }
            }
            if expanded {
                LessonInfo(lecturer: lesson.lecturer)
                    .transition(.opacity)
            }
        }
        .padding(12)
        .background(Color("surfaceVariant"))
        .clipShape(RoundedRectangle(cornerRadius: 28, style: .continuous))
        .onTapGesture {
            withAnimation {
                expanded.toggle()
            }
        }
    }
}

fileprivate struct LessonInfo: View {
    
    @State var lecturer: String?
    
    var body: some View {
        VStack {
            HStack {
                VStack(alignment: .leading) {
                    Text(Strings().get(id: SharedRes.strings().lecturer, args: []))
                        .font(golosFontFamily.medium(size: 12))
                        .foregroundStyle(Color("secondary"))
                    Text(lecturer ?? "–")
                        .font(golosFontFamily.regular(size: 18))
                        .foregroundStyle(Color("onBackground"))
                }
                Spacer()
                Button {
                    
                } label: {
                    Image(systemName: "info.circle")
                        .font(.system(size: 24))
                        .tint(Color("onBackground"))
                }
                .disabled(lecturer == nil)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(12)
        .background(Color("background"))
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
    }
}

fileprivate struct LessonOrdinal: View {
    
    @State var ordinal: Int
    
    var body: some View {
        VStack(spacing: 8) {
            Text("\(ordinal)")
                .monospacedDigit()
                .foregroundStyle(Color("inverseOnSurface"))
                .font(titleMediumStyle)
                .frame(width: 42, height: 42)
                .background(Color("onSurface"))
                .clipShape(RoundedRectangle(cornerRadius: 15, style: .continuous))
            RoundedRectangle(cornerRadius: 2)
                .frame(maxWidth: 2.5, maxHeight: .infinity)
                .foregroundColor(Color("onSurface"))
        }
    }
}

#Preview {
    let lesson = LessonViewData(
        name: "Математический анализ",
        lecturer: "Вестяк Владимир Анатольевич",
        ordinal: 1,
        type: .lecture,
        location: "ГУК Б-261"
    )
    
    return ScrollView(
        .vertical, content: { LessonCard(lesson: lesson).padding(12) })
    
}

extension View {
    func modify<T: View>(@ViewBuilder _ modifier: (Self) -> T) -> some View {
        return modifier(self)
    }
}

