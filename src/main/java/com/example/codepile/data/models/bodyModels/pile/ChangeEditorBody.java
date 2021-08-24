package com.example.codepile.data.models.bodyModels.pile;

import com.example.codepile.data.models.bodyModels.pile.base.BasePileBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeEditorBody extends BasePileBody {
    private String editorText;
}
