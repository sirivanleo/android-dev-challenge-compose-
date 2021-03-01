package com.example.androiddevchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

private const val PUPPY_ID_PARAM = "PUPPY_ID_PARAM"

/**
 * A simple [Fragment] subclass.
 * Use the [PuppyDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class PuppyDetail() : Fragment() {
    private var puppyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            puppyId = it.getString(PUPPY_ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    DetailScreen()
                }
            }
        }
    }

    @Composable
    fun DetailScreen() {
        Text("Test")
    }

    @Preview("Light Theme", widthDp = 360, heightDp = 640)
    @Composable
    fun LightPreview() {
        MaterialTheme {
            DetailScreen()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(puppyId: String) =
            PuppyDetail().apply {
                arguments = Bundle().apply {
                    putString(PUPPY_ID_PARAM, puppyId)
                }
            }
    }
}