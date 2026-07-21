import {useForm} from "react-hook-form";
import type {CdDTO} from "../types.tsx";
import {useEffect} from "react";

type FormValues = {
    title: string,
    performer: string,
    publicationYear: number | null,
    coverUrl: string | null
}
type CdPageProps = {
    onChangePage: (page: string) => void,
    onAddCd: (newCd: CdDTO) => void,
    onError: (message: string) => void
}

export default function AddCdPage(props: Readonly<CdPageProps>) {
    const { register, handleSubmit, reset,
        formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });

    function submit(data: FormValues) {
        //
    }

    useEffect(() => {
        props.onChangePage("add-cd")
    }, []);
    useEffect(() => {
        if (errors.title) {
            props.onError(errors.title.message!)
        } else if (errors.performer) {
            props.onError(errors.performer!.message!)
        } else {
            props.onError("")
        }
    }, [errors, props]);

    return (
        <div className="page">
            <form className="new-cd"
                  onSubmit={handleSubmit(submit)}>
                <label>
                    CD-Titel:
                    <input type="text"
                           {...register("title", {
                               required: "Der Titel der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label>
                    Interpret:
                    <input type="text"
                           {...register("performer", {
                               required: "Der Interpret der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label>
                    Jahr der Veröffentlichung:
                    <input type="text"
                           {...register("publicationYear")}
                    />
                </label>
            </form>
        </div>
    )
}