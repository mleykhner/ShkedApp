import SwiftUI
import shared

struct AttachmentView: View {
    let attachment: AttachmentViewData
    
    
    let fileService = FileNetworkService()
    
    @State var progress: Float = 0.0
    @State var path: String? = nil
    @State var isDownloading: Bool = false

	var body: some View {
        VStack(
            alignment: .center,
            spacing: 12
        ) {
            if #available(iOS 15.0, *) {
                AsyncImage(url: URL(string: attachment.previewURL)) { image in
                    image
                        .resizable()
                        .scaledToFill()
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 96, height: 120)
                .overlay {
                    if attachment.filePath == nil {
                        ZStack {
                            Rectangle()
                                .foregroundColor(Color.black.opacity(0.2))
                            if !isDownloading {
                                Button {
                                    isDownloading = true
                                    fileService.downloadFile(
                                        attachmentViewData: attachment,
                                        progress: { progress = $0.floatValue }
                                    ) { result, _ in
                                        if result != "" && result != nil {
                                            isDownloading = false
                                            path = result
                                        }
                                    }
                                } label: {
                                    Image(systemName: "arrow.down.to.line")
                                        .foregroundColor(.black)
                                        .background {
                                            Circle()
                                                .frame(
                                                    width: 48,
                                                    height: 48
                                                )
                                                .foregroundColor(.white.opacity(0.8))
                                        }
                                }
                            } else {
                                ZStack {
                                    Color.black.opacity(0.3)
                                    Button {
                                        // 
                                    } label: {
                                        ZStack {
                                            ProgressView(value: progress)
                                                .progressViewStyle(RingProgressViewStyle())
                                            Image(systemName: "stop.fill")
                                                .foregroundColor(Color.white)
                                        }
                                    }
                                    
                                    
                                }
                            }
                        }
                    }
                }
                .clipShape(
                    RoundedRectangle(
                        cornerRadius: 10,
                        style: .continuous
                    )
                )
            } else {
                //TODO: Реализовать для более старых версий
            }
            Text(attachment.fileName)
            Text(bytesShortenToString(attachment.sizeBytes))
        }
	}
}

func bytesShortenToString(_ bytes: Int64) -> String {
    var remains = Float(bytes)
    var order = 0
    while remains > 1024 && order < 3 {
        remains = remains / 1024
        order += 1
    }
    let number = String(format: "%.2f", remains)
    
    switch order {
    case 0: return number + " " + Strings().get(id: SharedRes.strings().b, args: [])
    case 1: return number + " " + Strings().get(id: SharedRes.strings().kb, args: [])
    default: return number + " " + Strings().get(id: SharedRes.strings().mb, args: [])
    }
}

struct AttachmentView_Previews: PreviewProvider {
    static let attachment = AttachmentViewData(
        id: NSUUID().uuidString,
        fileName: "test",
        extension: ".bin",
        previewURL: "https://i.pinimg.com/originals/e0/fe/0c/e0fe0c0f4443796cf1e158a99ff6068b.jpg",
        fileURL: "https://speed.hetzner.de/500MB.bin",
        filePath: nil,
        sizeBytes: 35_153_011
    )
	static var previews: some View {
        AttachmentView(attachment: attachment)
	}
}
