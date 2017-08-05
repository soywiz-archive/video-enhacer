package com.soywiz.videoenhacer

import com.soywiz.korio.async.EventLoop
import java.io.File

object Main {
	@JvmStatic fun main(args: Array<String>) = EventLoop {
		//VideoEnhacer.enhaceVideo(File("c:/temp/input.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input2.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input3.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input4.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input5.webm"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input5.webm"), scaleImage = 4, scaleTime = 4, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input6.gif"), scaleImage = 4, scaleTime = 2, smoothMotion = false)
		//VideoEnhacer.enhaceVideo(File("c:/temp/input7.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
		VideoEnhacer.enhaceVideo(File("c:/temp/input8.gif"), scaleImage = 2, scaleTime = 2, smoothMotion = false)
	}
}