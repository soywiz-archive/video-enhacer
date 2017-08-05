package com.soywiz.videoenhacer

import com.soywiz.korio.vfs.LocalVfs
import java.io.File

object VideoEnhacer {
	val FFMPEG = "ffmpeg"
	val WAIFU2X = "c:/dev/tools/waifu2x-caffe-cui"
	val BUTTERFLOW = "c:/dev/tools/butterflow-0.2.3/butterflow"

	suspend fun enhaceVideo(input: File, scaleImage: Int = 2, scaleTime: Int = 2, smoothMotion: Boolean = false) {
		val temp1 = File("${input.absolutePath}.temp.frames")
		val temp2x = File("${input.absolutePath}.temp.waifu2x.$scaleImage")
		val output = File("${input.absolutePath}.scaled.$scaleImage.mp4")
		val outputTS = File("${input.absolutePath}.scaled.$scaleImage.retimed.$scaleTime.smoothMotion.$smoothMotion.mp4")
		//waifu2x(File("c:/temp/0.png"), File("c:/temp/0.2x.png"), scale = 2)
		//println(ffmpegGetFps(input))
		if (!temp1.exists()) {
			temp1.mkdirs()
			ffmpegExtractFrames(input = input, outputFolder = temp1)
		}
		if (!temp2x.exists()) {
			temp2x.mkdirs()
			waifu2x(temp1, temp2x, scale = scaleImage)
		}
		if (!output.exists()) {
			val fps = ffmpegGetFps(input)
			ffmpegRepackFrames(temp2x, output, fps = fps)
		}
		if (!outputTS.exists()) {
			butterflow(output, outputTS, scale = scaleTime, smoothMotion = smoothMotion)
		}
	}


	suspend fun ffmpegGetFps(input: File): Double {
		// ffprobe -v 0 -of compact=p=0 -select_streams 0 -show_entries stream=r_frame_rate c:\temp\input.gif

		val args = listOf(FFMPEG, "-i", input.absolutePath)
		val result = LocalVfs(input.parentFile).execToString(args, captureError = true, throwOnError = false)
		val res = Regex("([\\d\\.]+)\\s+fps").find(result)

		return res?.groupValues?.get(1)?.toDoubleOrNull() ?: throw Exception("Can't get fps in : " + result)
	}

	suspend fun ffmpegExtractFrames(input: File, outputFolder: File) {
		outputFolder.mkdirs()
		val args = listOf(
				FFMPEG,
				"-i", input.absolutePath,
				"-c:v", "png",
				"-f", "image2",
				"${outputFolder.absolutePath}/out%06d.png"
		)
		println(args.joinToString(" "))
		LocalVfs(outputFolder.parentFile).execToString(args)
	}

	suspend fun ffmpegRepackFrames(inputFolder: File, output: File, fps: Double) {
		val args = listOf(
				FFMPEG, "-r", "14",
				"-i", "${inputFolder.absolutePath}/out%06d.png",
				"-c:v", "libx264",
				"-vf",
				"fps=$fps",
				"-pix_fmt",
				"yuv420p",
				output.absolutePath
		)
		println(args.joinToString(" "))
		LocalVfs(output.parentFile).execToString(args)
	}

	suspend fun waifu2x(input: File, output: File, noise: Int = 3, scale: Int = 2) {
		val args = listOf(
				WAIFU2X,
				//"--gpu", "0",
				//"-p", "cpu",
				"-p", "gpu",
				"-s", "$scale",
				"-n", "$noise",
				"-i", input.absolutePath,
				"-o", output.absolutePath
		)
		println(args.joinToString(" "))
		LocalVfs(output.parentFile).execToString(args)
	}

	suspend fun butterflow(input: File, output: File, scale: Int = 2, smoothMotion: Boolean = false) {
		val args = listOf(
				BUTTERFLOW,
				input.absolutePath,
				"-o",
				output.absolutePath,
				"-r", "${scale}x",
				"-audio"
		) + (if (smoothMotion) listOf("-sm") else listOf())
		println(args.joinToString(" "))
		LocalVfs(output.parentFile).execToString(args)
	}
}