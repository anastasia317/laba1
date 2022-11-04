package com.example.laba1

import javafx.animation.FadeTransition
import javafx.animation.PathTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.shape.MoveTo
import javafx.scene.shape.LineTo
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.*
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.io.File

enum class ImageStyle {
    CIRCLE,
    RECTANGLE
}

enum class AnimationType {
    FADE_TRANSITION,
    TRANSLATE_TRANSITION
}

enum class Position {
    RIGHT_TOP,
    RIGHT_BOTTOM,
    LEFT_TOP,
    LEFT_BOTTOM
}
enum class ButtonsQuantity {
    ONE_BUTTON,
    TWO_BUTTONS
}


class Config {
    val position = Position.RIGHT_BOTTOM
    val titleCloseButton = "Close"
    val titleReplyButton = "Reply"
    var alpha = 0.9
    var openTime = 7000.0
    var imageType = ImageStyle.CIRCLE
    var animation = AnimationType.TRANSLATE_TRANSITION
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Telegram_2019_Logo.svg/640px-Telegram_2019_Logo.svg.png"
    var notificationSound = "C:\\Users\\админ\\IdeaProjects\\laba1\\src\\main\\resources\\out-of-nowhere-message-tone.mp3"
    var buttonsQuantity = ButtonsQuantity.TWO_BUTTONS
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()
    private var primaryScreenBounds = Screen.getPrimary().visualBounds

    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun build(): Toast {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }


    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)

        var width = 300.0

        windows.scene = Scene(root)
        windows.width = width
        windows.scene.fill = Color.TRANSPARENT

        root.style = "-fx-padding: 15px; -fx-margin: 15px"
        root.prefWidth = width

        setImage()

        val vbox = VBox()

        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)

        title.style = "-fx-font-family: Franklin-Gothic-Book; -fx-font-weight: BOLD; -fx-font-size: 22px"
        title.maxWidth = 175.0
        title.isWrapText = true

        message.style = "-fx-font-family: Franklin-Gothic-Book; -fx-font-size: 18px; -fx-text-fill: #5c606a"
        message.maxWidth = 175.0
        message.isWrapText = true

        appName.style = "-fx-font-family: Franklin-Gothic-Book; -fx-font-size: 18px; -fx-text-fill: #5c606a"
        appName.maxWidth = 175.0
        appName.isWrapText = true

        vbox.children.addAll(title, message, appName)
        box.children.add(vbox)
        box.style = "-fx-spacing: 10px"
        root.center = box
        addButtons()

    }

    private fun onReplyButton() {
        val reply = BorderPane()
        val textField = TextField("Your Reply")
        val sendButton = Button("Send")

        val eventSendButton = EventHandler<ActionEvent>() {
            closeAnimation(reply)
        }

        sendButton.style = "-fx-background-color: #ffffff; -fx-font-size: 18px; -fx-background-radius: 3,2,2,2; -fx-font-weight: 500; -fx-border-color: black"
        sendButton.maxWidth = Double.MAX_VALUE
        sendButton.onAction = eventSendButton
        HBox.setHgrow(sendButton, Priority.ALWAYS)

        textField.style = "-fx-border-color: black; -fx-font-size: 18px"

        reply.style = "-fx-padding: 15px; -fx-margin: 10px"
        reply.center = textField
        reply.bottom = sendButton

        windows.scene = Scene(reply, windows.width, windows.height)
        windows.scene.fill = Color.TRANSPARENT

    }

    private fun addButtons() {
        var buttons = HBox()
        val closeButton = Button(config.titleCloseButton)
        val replyButton = Button(config.titleReplyButton)

        val eventCloseButton = EventHandler<ActionEvent>() {
            closeAnimation(root)
        }
        val eventReplyButton = EventHandler<ActionEvent>() {
            onReplyButton()
        }

        closeButton.style = "-fx-background-color: #ffffff; -fx-font-size: 18px; -fx-background-radius: 3,2,2,2; -fx-font-weight: 500; -fx-border-color: black"
        closeButton.maxWidth = Double.MAX_VALUE
        closeButton.onAction = eventCloseButton
        HBox.setHgrow(closeButton, Priority.ALWAYS)

        replyButton.style = "-fx-background-color: #ffffff; -fx-font-size: 18px; -fx-background-radius: 3,2,2,2; -fx-font-weight: 500; -fx-border-color: black"
        replyButton.maxWidth = Double.MAX_VALUE
        replyButton.onAction = eventReplyButton
        HBox.setHgrow(replyButton, Priority.ALWAYS)

        if (config.buttonsQuantity == ButtonsQuantity.ONE_BUTTON) {
            buttons.children.add(closeButton)
        } else {
            buttons.children.addAll(replyButton, closeButton)
        }
        buttons.style = "-fx-spacing: 10px; -fx-padding: 10px 0px 0px 0px"
        root.bottom = buttons
    }

    private fun playSound() {
        val media = Media(File(config.notificationSound).toURI().toString())
        val player = MediaPlayer(media)
        player.play()
    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(100.0, 100.0)
        } else {
            Circle(50.0, 50.0, 50.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)


    }

    private fun getWindowsCoords() {
        if (config.position == Position.LEFT_TOP) {
            windows.x = 0.0
            windows.y = 0.0
        }

        if (config.position == Position.LEFT_BOTTOM) {
            windows.x = 0.0
            windows.y = primaryScreenBounds.height - windows.height
        }

        if (config.position == Position.RIGHT_TOP) {
            windows.x = primaryScreenBounds.width - windows.width
            windows.y = 0.0
        }

        if (config.position == Position.RIGHT_BOTTOM) {
            windows.x = primaryScreenBounds.width - windows.width
            windows.y = primaryScreenBounds.height - windows.height
        }
    }

    private fun openAnimation() {
        getWindowsCoords()

        if (config.animation == AnimationType.FADE_TRANSITION) {
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = 0.0
            anim.toValue = config.alpha
            anim.cycleCount = 1
            anim.play()
        } else {
            val anim = TranslateTransition(Duration.millis(1500.0), root)
            if ((config.position == Position.LEFT_TOP) or (config.position == Position.LEFT_BOTTOM)) {
                anim.fromX = windows.x - windows.width
                anim.toX = windows.x
            } else {
                anim.fromX = 0.0 + windows.width
                anim.toX = 0.0
            }
            anim.cycleCount = 1
            anim.play()
        }
    }

    private fun closeAnimation(root: Node) {
        if (config.animation == AnimationType.FADE_TRANSITION) {
            val anim = FadeTransition(Duration.millis(1500.0), root)
            anim.fromValue = config.alpha
            anim.toValue = 0.0
            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        } else {
            val anim = TranslateTransition(Duration.millis(1500.0), root)
            if ((config.position == Position.LEFT_TOP) or (config.position == Position.LEFT_BOTTOM)) {
                anim.fromX = windows.x
                anim.toX = windows.x - windows.width
            } else {
                anim.fromX = 0.0
                anim.toX = 0.0 + windows.width
            }
            anim.cycleCount = 1
            anim.onFinished = EventHandler {
                Platform.exit()
                System.exit(0)
            }
            anim.play()
        }
    }

    fun start() {
        windows.show()
        openAnimation()
        playSound()
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation(root)
        }
        Thread(thread).start()
    }

}


class SomeClass : Application() {
    override fun start(p0: Stage?) {
        var toast = Toast.Builder()
            .setTitle("New message")
            .setMessage("Hello World")
            .setAppName("Telegram")
            .build()
        toast.start()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}



